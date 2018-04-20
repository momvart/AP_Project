package models.soldiers;

import models.Attack;
import models.buildings.Building;
import utils.Point;

import java.util.ArrayList;
import java.util.Collections;

public class GeneralAttackHelper extends AttackHelper

{
    private Building target;

    public GeneralAttackHelper(Attack attack, Building favouriteTarget, Point location, int soldierDamagePotential, int soldierRange)
    {
        super(attack, favouriteTarget, location, soldierDamagePotential, soldierRange);
    }

    @Override
    public void move()
    {

    }

    @Override
    public void fire()
    {
        if (isTargetInRange())
        {
            target.decreaseStrength(getSoldierDamagePotential());
        }
    }

    private boolean isTargetInRange()
    {
        return euclidianDistance(target.getLocation(), getSoldierLocation()) <= getSoldierRange();
    }


    @Override
    public void setTarget()
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

    private Building getNearestBuilding()
    {
        if (attack.getMap().getBuildings() != null)
        {
            ArrayList<Building> buildings = attack.getMap().getBuildings();
            ArrayList<Integer> distances = new ArrayList<>();
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
            if (tower.getType() == super.getSoldierFavouriteTarget().getType())
            {
                favoutriteTargets.add(tower);
            }
        }
        ArrayList<Integer> manhatanianDistances = new ArrayList<>();
        if (favoutriteTargets.size() != 0)
        {
            for (Building favoutriteTarget : favoutriteTargets)
            {
                manhatanianDistances.add(manhatanianDistance (favoutriteTarget.getLocation() , super.getSoldierLocation()));
            }
            Collections.sort(manhatanianDistances);
            for (Building favoutriteTarget : favoutriteTargets)
            {
                for (int i = 0; i <manhatanianDistances.size() ; i++)
                {
                    if (manhatanianDistance(favoutriteTarget.getLocation() , super.getSoldierLocation()) == manhatanianDistances.get(i))
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
        return manhatanianDistance(getSoldierLocation() , favoutriteTarget.getLocation()) < 24;//TODO to be manipulated for increasing the performance
    }

    private boolean isTargetReachable(Building favouriteTarget)
    {
        //TODO we should check if there is a road to the target (including the 4sideOpenity of target)
        return false;
    }
}
