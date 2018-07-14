package models.attack.attackHelpers;

import exceptions.DummyException;
import graphics.helpers.GeneralSoldierGraphicHelper;
import graphics.helpers.SoldierGraphicHelper;
import models.Resource;
import models.attack.Attack;
import models.buildings.*;
import models.soldiers.MoveType;
import models.soldiers.Soldier;
import utils.Point;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class GeneralSoldierAttackHelper extends SoldierAttackHelper
{
    private Building target;

    public GeneralSoldierAttackHelper(Attack attack, Soldier soldier)
    {
        super(attack, soldier);
    }

    public Building getTarget()
    {
        return target;
    }

    private IOnSoldierFireListener soldierFireListener;

    public void setSoldierFireListener(IOnSoldierFireListener soldierFireListener)
    {
        this.soldierFireListener = soldierFireListener;
    }

    private void callOnSoldierFire(BuildingDestructionReport bdr)
    {
        if (soldierFireListener != null)
            soldierFireListener.onSoldierFire(bdr);
    }

    @Override
    public void fire()
    {
        if (!isReal)
            return;
        if (soldier == null || soldier.getAttackHelper().isDead())
            return;
        if (target == null)
            return;

        if (target.getStrength() > 0 && !target.getAttackHelper().isDestroyed())
        {
            int initialStrength = target.getStrength();

            target.getAttackHelper().decreaseStrength(getDamage(), false);
            BuildingDestructionReport bdr = new BuildingDestructionReport(target, initialStrength, target.getStrength());
            callOnSoldierFire(bdr);
            if (target instanceof Storage)
            {
                Storage storage = (Storage)target;

                int finalStrength = Math.max(storage.getStrength(), 0);
                int damage = initialStrength - finalStrength;

                int claimedAmount = finalStrength == 0 ? storage.getCurrentAmount() : (int)((float)storage.getCurrentAmount() * damage / initialStrength);

                if (storage instanceof GoldStorage)
                {
                    attack.addToClaimedResource(new Resource(claimedAmount, 0));
                    NetworkHelper.setClaimedResource(attack.getClaimedResource());
                }

                else
                {
                    attack.addToClaimedResource(new Resource(0, claimedAmount));
                    NetworkHelper.setClaimedResource(attack.getClaimedResource());
                }
                storage.decreaseCurrentAmount(claimedAmount);
            }
        }

        if (target.getStrength() <= 0)
        {
            attack.addScore(target.getBuildingInfo().getDestroyScore());
            NetworkHelper.setClaimedScore(attack.getClaimedScore());
            attack.addToClaimedResource(target.getBuildingInfo().getDestroyResource());
            NetworkHelper.setClaimedResource(attack.getClaimedResource());
        }
    }

    private boolean isTargetInRange()
    {
        if (target != null)
        {
            double distance2nd = Point.euclideanDistance2nd(target.getLocation(), getSoldierLocation());
            return distance2nd == 2 || Math.sqrt(distance2nd) <= getRange();
        }
        return true;
        //TODO‌ bad smell of redundant code of escaping compile error.in fact the code shouldn't reach here because we set the target first then we come up to move or fight
    }

    @Override
    public void setTarget()
    {
        if (!isReal)
            return;
        if (soldier != null && !soldier.getAttackHelper().isDead())
            if (target == null || target.getAttackHelper().getStrength() <= 0 || target.getAttackHelper().isDestroyed())
            {
                try
                {
                    target = getNearestBuilding();
                }
                catch (Exception e) {}
                if (target != null)
                    System.err.println("new target: " + target.getName() + " at: " + target.getLocation().toString());
                else
                    System.err.println("no target found.");
            }
    }

    @Override
    public Point getTargetLocation()
    {
        if (target == null)
            return new Point(-1, -1);
        return target.getLocation();
    }

    private Building getNearestBuilding() throws Exception
    {
        ArrayList<Building> aliveBuildings = Attack.getAliveBuildings(soldier.getAttackHelper().getAttack())
                .sorted(Comparator.comparingDouble(building -> Point.euclideanDistance2nd(building.getLocation(), getSoldierLocation())))
                .collect(Collectors.toCollection(ArrayList::new));

        Function<Building, Building> setRealTarget = target ->
        {
            if (target == null)
                return null;
            if (isTargetReachable(target))
                return target;

            ArrayList<Point> path = attack.getSoldierPath(getSoldierLocation(), target.getLocation(), true);
            for (int i = path.size() - 1; i >= 0; i--)
                if (attack.getMap().getBuildingAt(path.get(i)) != null)
                    return attack.getMap().getBuildingAt(path.get(i));

            return null;
        };


        try
        {
            if (soldier.getSoldierInfo().getFavouriteTargets().length == 0)
                throw new Exception();

            return setRealTarget.apply(aliveBuildings.stream()
                    .filter(building -> Arrays.stream(soldier.getSoldierInfo().getFavouriteTargets()).anyMatch(t -> t.isInstance(building)))
                    .findFirst().orElseThrow(Exception::new));
        }
        catch (Exception ex)
        {
            return setRealTarget.apply(aliveBuildings.stream()
                    .filter(building -> building.getType() != Trap.BUILDING_TYPE)
                    .filter(building -> building.getType() != Wall.BUILDING_TYPE)
                    .findFirst()
                    .orElse(null));
        }
    }

    private Building getCriticalWall(List<Point> soldierPath) throws Exception
    {
        for (int i = 1; i < soldierPath.size(); i++)
            if (isThereAWallIn(soldierPath.get(i)))
                return getBuildingIn(soldierPath.get(i));
        throw new Exception();
    }

    private Building getBuildingIn(Point point) throws Exception
    {
        List<Building> aliveBuildings = Attack.getAliveBuildings(attack).collect(Collectors.toList());
        for (Building aliveBuilding : aliveBuildings)
            if (aliveBuilding.getLocation().equals(point))
                return aliveBuilding;
        throw new Exception();
    }

    private boolean isThereAWallIn(Point point)
    {
        List<Building> aliveBuildings = Attack.getAliveBuildings(attack).collect(Collectors.toList());
        for (Building aliveBuilding : aliveBuildings)
            if (aliveBuilding instanceof Wall && aliveBuilding.getLocation().equals(point))
                return true;
        return false;
    }

    private boolean isTargetReachable(Building building)
    {
        try
        {
            return attack.getSoldierPath(soldier.getLocation(), building.getLocation(), soldier.getMoveType() == MoveType.AIR) != null;
        }
        catch (Exception e)
        {
            return false;
        }

    }

    @Override
    public void move()
    {
        if (soldier != null && !soldier.getAttackHelper().isDead())
            if (!isTargetInRange())
            {
                Point pointToGo = getPointToGo(target.getLocation());
                attack.moveOnLocation(soldier, getSoldierLocation(), pointToGo);
                soldier.setLocation(pointToGo);
            }
    }

    //graphic phase

    @Override
    public void setGraphicHelper(SoldierGraphicHelper graphicHelper)
    {
        if (!(graphicHelper instanceof GeneralSoldierGraphicHelper))
            throw new IllegalArgumentException("Graphic helper should be a GeneralSoldierGraphicHelper.");

        super.setGraphicHelper(graphicHelper);
    }

    @Override
    public void onReload()
    {
        super.onReload();
        if (isSoldierDeployed() && (soldier == null || isDead() || getHealth() <= 0))
        {
            callOnSoldierDie();
            return;
        }
        if (readyToFireTarget)
            if (soldier != null && isSoldierDeployed() && !isDead() && getHealth() > 0)
            {
                if (target == null || target.getStrength() <= 0 || target.getAttackHelper().isDestroyed())
                {
                    setTarget();
                    callOnDecamp();
                    return;
                }
                fire();
            }
    }
}
