package models.soldiers;

import models.Attack;
import models.buildings.Building;
import models.buildings.DefensiveTower;
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

    private DefensiveTower getNearestBuilding()
    {
        ArrayList<DefensiveTower> towers = attack.getMap().getDefensiveTowers();
        for (int i = 1; i <= 60  ; i++)
        {
            for (DefensiveTower tower : towers)
            {
                if (manhatanianDistance(getSoldierLocation() ,tower.getLocation()) == i)
                {
                    return tower;
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

    private boolean isTargetReachable(Building favoutriteTarget)
    {
        //TODO we should check if there is a road to the target (including the 4sideOpenity of target)
    }
}
