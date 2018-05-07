package models.soldiers;

import models.Attack;
import models.Resource;
import models.buildings.*;
import utils.Point;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GeneralAttackHelper extends AttackHelper
{
    private Building target;

    public GeneralAttackHelper(Attack attack, Soldier soldier)
    {
        super(attack, soldier);
    }

    @Override
    public void fire()
    {
        if (soldier == null || soldier.getAttackHelper().isDead())
            return;
        if (target == null)
            return;
        if (!isTargetInRange())
            return;

        if (target.getStrength() > 0 && !target.isDestroyed())
        {
            int initialStrength = target.getStrength();
            target.decreaseStrength(getDamage());

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
//                switch (target.getType())
//                {
//                    case GoldStorage.BUILDING_TYPE:
//                    {
//                        Resource resourceClaimed = new Resource((int)Math.floor(1.0 * damageRael / (BuildingValues.getBuildingInfo(target.getType()).getInitialStrength() + target.getLevel() * 10) * storage.getCurrentAmount()), 0);// 10 may vary in the up and coming configs
//                        attack.addToClaimedResource(resourceClaimed);
//                        attack.addToGainedResourceOfStorageDesroying(storage, resourceClaimed);
//                    }
//                    case ElixirStorage.BUILDING_TYPE:
//                    {
//                        Resource resourceClaimed = new Resource(0, (int)Math.floor(1.0 * damageRael / (BuildingValues.getBuildingInfo(target.getType()).getInitialStrength() + target.getLevel() * 10) * storage.getCurrentAmount()));// 10 may vary in the up and coming configs
//                        attack.addToClaimedResource(resourceClaimed); // 10 may vary in the up and coming configs
//                        attack.addToGainedResourceOfStorageDesroying(storage, resourceClaimed);
//                    }
//                }
            }
        }

        if (target.getStrength() <= 0)
        {
            target.setDestroyed(true);
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
            if (target == null || target.getStrength() <= 0 || target.isDestroyed())
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
                    .findFirst()
                    .orElse(null);
        }
    }


    public Stream<Building> getAliveBuildings()
    {
        return attack.getMap().getBuildings().stream().filter(building -> !building.isDestroyed());
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
}
