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
import java.util.List;
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
                try
                {
                    target = getNearestBuilding();
                }
                catch (Exception e)
                {
                }
                if (target != null)
                    System.err.println("new target: " + target.getName() + " at: " + target.getLocation().toString());
                else
                    System.err.println("no target found.");
            }
    }

    private Building getNearestBuilding() throws Exception
    {
        ArrayList<Building> aliveBuildings = getAliveBuildings()
                .sorted(Comparator.comparingDouble(building -> Point.euclideanDistance2nd(building.getLocation(), getSoldierLocation())))
                .collect(Collectors.toCollection(ArrayList::new));
        ArrayList<Building> buildingsWithoutWalls = getAliveBuildings()
                .sorted(Comparator.comparingDouble(building -> Point.euclideanDistance2nd(building.getLocation(), getSoldierLocation())))
                .filter(building -> building.getType() != Wall.BUILDING_TYPE)
                .collect(Collectors.toCollection(ArrayList::new));

        if (aliveBuildings == null || buildingsWithoutWalls == null || aliveBuildings.size() == 0 || buildingsWithoutWalls.size() == 0)
        {
            throw new Exception("no buildings there is on the map");
        }
        try
        {
            List<Building> favouriteBuildings = aliveBuildings.stream()
                    .filter(building -> Arrays.stream(soldier.getSoldierInfo().getFavouriteTargets()).anyMatch(t -> t.isInstance(building)))
                    .collect(Collectors.toList());

            if (soldier.getSoldierInfo().getFavouriteTargets().length == 0)
            {
                throw new Exception();
            }
            else
            {
                if (isThereAFavouriteBuildingIn(aliveBuildings))
                {
                    Building building = favouriteBuildings.get(0);
                    if (isTargetReachable(building))
                    {
                        return building;
                    }
                    else
                    {
                        throw new Exception();// to be manipulated later on we should return the cutting edge wall on the path to the soldier point
                    }
                }
                else
                {
                    throw new Exception();
                }
            }
        }
        catch (Exception ex)
        {
            Building building = buildingsWithoutWalls.get(0);
            if (isTargetReachable(building))
            {
                return building;
            }
            else
            {
                return aliveBuildings.get(0);
            }
        }
    }

    private boolean isThereAFavouriteBuildingIn(ArrayList<Building> aliveBuildings)
    {
        return aliveBuildings.stream()
                .filter(building -> Arrays.stream(soldier.getSoldierInfo().getFavouriteTargets()).anyMatch(t -> t.isInstance(building)))
                .collect(Collectors.toList()).size() > 0;
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
