package models.attack.attackHelpers;

import graphics.helpers.GeneralSoldierGraphicHelper;
import graphics.helpers.SoldierGraphicHelper;
import models.Resource;
import models.attack.Attack;
import models.buildings.*;
import models.soldiers.MoveType;
import models.soldiers.Soldier;
import utils.Point;
import utils.PointF;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
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
        System.out.println("soldier firing.. ");
        if (soldier == null || soldier.getAttackHelper().isDead())
            return;
        if (target == null)
            return;

        if (target.getStrength() > 0 && !target.getAttackHelper().isDestroyed())
        {
            int initialStrength = target.getStrength();
            target.getAttackHelper().decreaseStrength(getDamage());

            BuildingDestructionReport bdr = new BuildingDestructionReport(target, initialStrength, target.getStrength());
            callOnSoldierFire(bdr);
            if (target instanceof Storage)
            {
                Storage storage = (Storage)target;

                int finalStrength = Math.max(storage.getStrength(), 0);
                int damage = initialStrength - finalStrength;

                int claimedAmount = finalStrength == 0 ? storage.getCurrentAmount() : (int)((float)storage.getCurrentAmount() * damage / initialStrength);

                if (storage instanceof GoldStorage)
                    attack.addToClaimedResource(new Resource(claimedAmount, 0));
                else
                    attack.addToClaimedResource(new Resource(0, claimedAmount));

                storage.decreaseCurrentAmount(claimedAmount);
            }
        }

        if (target.getStrength() <= 0)
        {
            attack.addScore(target.getBuildingInfo().getDestroyScore());
            attack.addToClaimedResource(target.getBuildingInfo().getDestroyResource());
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
        if (soldier != null && !soldier.getAttackHelper().isDead())
            if (target == null || target.getAttackHelper().getStrength() <= 0 || target.getAttackHelper().isDestroyed())
            {
                target = getNearestBuilding();
                if (target != null)
                    System.err.println("new target: " + target.getName() + " at: " + target.getLocation().toString());
                else
                    System.err.println("no target found.");
            }
    }

    private Building getNearestBuilding()
    {
        ArrayList<Building> aliveBuildings = getAliveBuildings()
                .sorted(Comparator.comparingDouble(building -> Point.euclideanDistance2nd(building.getLocation(), getSoldierLocation())))
                .collect(Collectors.toCollection(ArrayList::new));
        try
        {
            if (soldier.getSoldierInfo().getFavouriteTargets().length == 0)
                throw new Exception();

            return aliveBuildings.stream()
                    .filter(building -> Arrays.stream(soldier.getSoldierInfo().getFavouriteTargets()).anyMatch(t -> t.isInstance(building)))
                    .filter(this::isTargetReachable)
                    .findFirst().orElseThrow(Exception::new);

        }
        catch (Exception ex)
        {
            return aliveBuildings.stream()
                    .filter(this::isTargetReachable)
                    .filter(building -> building.getType() != Trap.BUILDING_TYPE) //canceling out the traps from the target choices
                    .findFirst()
                    .orElse(null);
        }
    }


    private boolean isTargetReachable(Building favouriteTarget)
    {
        return !(attack.getSoldierPath(soldier.getLocation(), favouriteTarget.getLocation(), soldier.getMoveType() == MoveType.AIR) == null);
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


    private boolean readyToFireTarget = false;



    @Override
    public void onMoveFinished(PointF currentPos)
    {
        readyToFireTarget = true;
    }


    @Override
    public void setGraphicHelper(SoldierGraphicHelper graphicHelper)
    {
        if (!(graphicHelper instanceof GeneralSoldierGraphicHelper))
            throw new IllegalArgumentException("Graphic helper should be a GeneralSoldierGraphicHelper.");

        super.setGraphicHelper(graphicHelper);

        GeneralSoldierGraphicHelper gh = (GeneralSoldierGraphicHelper)graphicHelper;
        this.setSoldierFireListener(gh);
    }

    @Override
    public void onReload()
    {
        if (isSoldierDeployed() && (soldier == null || isDead || getHealth() <= 0))
        {
            callOnSoldierDie();
            return;
        }
        if (readyToFireTarget)
        {
            if (soldier != null && isSoldierDeployed() && !isDead && getHealth() > 0)
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
}
