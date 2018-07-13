package models.attack.attackHelpers;

import graphics.helpers.IOnMoveFinishedListener;
import models.attack.Attack;
import models.buildings.DefensiveTower;
import models.buildings.GuardianGiant;
import models.soldiers.Soldier;
import utils.Point;
import utils.PointF;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class GuardianGiantAttackHelper extends SingleTargetAttackHelper implements IOnMoveFinishedListener
{
    public GuardianGiantAttackHelper(GuardianGiant building, Attack attack)
    {
        super(building, attack);
    }

    public Soldier getTargetSoldier()
    {
        return targetSoldier;
    }

    private IOnDecampListener decampListener;
    private boolean readyToFireTarget = false;

    private void move()
    {
        Point pointToGo = getPointToGo(targetSoldier.getLocation());
        attack.getMap().changeBuildingCell(building, pointToGo);
    }

    private Point getPointToGo(Point destination)
    {
        List<Point> soldierPath = attack.getSoldierPath(building.getLocation(), destination, false);
        Point pointToGo = soldierPath.get(soldierPath.size() - 1);

        int i;
        for (i = soldierPath.size() - 1; i >= 0; i--)
        {
            if (i != soldierPath.size() - 1)
                pointToGo = soldierPath.get(i + 1);
            if (Point.euclideanDistance(soldierPath.get(i), building.getLocation()) > GuardianGiant.GUARDIAN_GIANT_SPEED)
                break;
        }
        return pointToGo;
    }

    public Point getTargetLocation()
    {
        return targetSoldier.getLocation();
    }

    public Point getLastPointOfStanding(int range, Point start, Point destination)
    {
        List<Point> soldierPath = attack.getSoldierPath(start, destination, false);
        if (soldierPath == null || soldierPath.size() <= 1)
            return null;
        Point lastPoint = soldierPath.get(1);

        int i;
        for (i = 1; i < soldierPath.size() - 1; i++)
        {
            lastPoint = soldierPath.get(i);
            if (Point.euclideanDistance(soldierPath.get(i + 1), destination) > range)
            {
                break;
            }
        }
        return lastPoint;
    }

    @Override
    public void setTarget(boolean networkPermission)
    {
        if (!isReal && !networkPermission)
            return;
        mainTargets = new ArrayList<>();
        Optional<Soldier> min = attack.getDeployedAliveUnits().min(Comparator.comparingDouble(soldier -> Point.euclideanDistance2nd(soldier.getLocation(), getBuilding().getLocation())));
        min.ifPresent(soldier -> mainTargets.add(soldier));
        if (mainTargets != null && mainTargets.size() != 0)
            targetSoldier = mainTargets.get(0);
        else
            targetSoldier = null;
        if (isReal)
            NetworkHelper.buildingSetTarget(building.getId());
    }

    @Override
    public void passTurn()
    {
        if (!destroyed)
        {
            DefensiveTower defensiveTower = (DefensiveTower)building;
            setTarget(false);
            if (targetSoldier != null && Point.euclideanDistance(targetSoldier.getLocation(), defensiveTower.getLocation()) <= defensiveTower.getRange() * 1.5)
                attack();
            else
                move();
        }
    }

    public void setDecampListener(IOnDecampListener decampListener)
    {
        this.decampListener = decampListener;
    }

    @Override
    public void onMoveFinished(PointF currentPos)
    {
        readyToFireTarget = true;
    }

    @Override
    public void onReload()
    {
        if (building == null || getStrength() <= 0 || isDestroyed())
        {
            callOnDestroyed();
            return;
        }
        if (readyToFireTarget)
            if (building != null && getStrength() > 0)
            {
                if (targetSoldier == null || targetSoldier.getAttackHelper().getHealth() <= 0 || targetSoldier.getAttackHelper().isDead())
                {
                    setTarget(false);
                    callOnDecamp();
                    return;
                }
                attack();
            }
    }

    private void callOnDecamp()
    {
        readyToFireTarget = false;
        if (decampListener != null)
            decampListener.onDecamp();
    }
}
