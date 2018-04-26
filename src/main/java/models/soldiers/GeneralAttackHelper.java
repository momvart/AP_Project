package models.soldiers;

import models.Attack;
import models.buildings.Building;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class GeneralAttackHelper extends AttackHelper

{
    private Building target;

    public GeneralAttackHelper(Attack attack, Soldier soldier)
    {
        super(attack, soldier);
    }

    @Override
    public void move()
    {
        if (soldier != null && !soldier.getAttackHelper().isDead())
        {
            if (!isTargetInRange())
            {
                //TODO‌ to be done by the code Mahdi is working on.
            }
        }
    }

    @Override
    public void fire()
    {
        if (soldier != null && !soldier.getAttackHelper().isDead())
        {
            if (isTargetInRange())
            {
                target.decreaseStrength(getDamage());
            }
        }
    }

    private boolean isTargetInRange()
    {
        return euclidianDistance(target.getLocation(), getSoldierLocation()) <= getRange();
    }

    @Override
    public void setTarget()
    {
        if (soldier != null && !soldier.getAttackHelper().isDead())
        {
            if (target == null || target.getStrength() <= 0)
            {
                if (getBestFavouriteTarget() != null)
                {
                    target = getBestFavouriteTarget();
                }
                else
                {
                    target = getNearestBuilding();
                }
            }
        }
    }

    private Building getNearestBuilding()
    {
        if (attack.getMap().getBuildings() != null)
        {
            ArrayList<Building> buildings = attack.getMap().getBuildings();
            ArrayList<Double> distances = new ArrayList<>();
            for (Building building : buildings)
            {
                distances.add(euclidianDistance(building.getLocation(), getSoldierLocation()));
            }
            Collections.sort(distances);

            for (Building building : buildings)
            {
                if (euclidianDistance(building.getLocation(), getSoldierLocation()) == distances.get(0))
                {
                    return building;
                }
            }
        }
        return null;
    }

    public Building getBestFavouriteTarget()
    {
        ArrayList<Building> favoutriteTargets = new ArrayList<>();
        ArrayList<Building> towers = super.attack.getMap().getBuildings();
        for (Building tower : towers)
        {
            if (Arrays.stream(soldier.getSoldierInfo().getFavouriteTargets()).anyMatch(c -> c.isInstance(tower)))
            {
                favoutriteTargets.add(tower);
            }
        }
        ArrayList<Integer> manhatanianDistances = new ArrayList<>();
        if (favoutriteTargets.size() != 0)
        {
            for (Building favoutriteTarget : favoutriteTargets)
            {
                manhatanianDistances.add(manhatanianDistance(favoutriteTarget.getLocation(), super.getSoldierLocation()));
            }
            Collections.sort(manhatanianDistances);
            for (Building favoutriteTarget : favoutriteTargets)
            {
                for (int i = 0; i < manhatanianDistances.size(); i++)
                {
                    if (manhatanianDistance(favoutriteTarget.getLocation(), super.getSoldierLocation()) == manhatanianDistances.get(i))
                    {
                        if (isTargetReachable(favoutriteTarget) && isntTargetTooFar(favoutriteTarget))
                        {
                            return favoutriteTarget;
                        }
                    }
                }
            }
        }
        return null;
    }

    private boolean isntTargetTooFar(Building favoutriteTarget)
    {
        return manhatanianDistance(getSoldierLocation(), favoutriteTarget.getLocation()) < 24;//TODO to be manipulated for increasing the performance
    }

    private boolean isTargetReachable(Building favouriteTarget)
    {
        //attack.getMap().isReachable();
        return true;
    }
}
