package models.soldiers;

import models.Attack;
import models.buildings.Building;
import models.buildings.DefensiveTower;
import sun.nio.cs.ext.EUC_JP_LINUX;
import utils.Point;

import java.util.ArrayList;
import java.util.Collections;
import java.util.zip.DataFormatException;

public class GeneralAttackHelper extends AttackHelper

{
    private Building target;

    public GeneralAttackHelper(Attack attack)
    {
        super(attack, null, null);
    }

    @Override
    public void move()
    {

    }

    @Override
    public void fire()
    {

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
        ArrayList<DefensiveTower> favoutriteTargets = new ArrayList<>();
        ArrayList <DefensiveTower> towers = super.attack.getMap().getDefensiveTowers();
        for (DefensiveTower tower : towers)
        {
            if (tower.getType() == super.getSoldierFavouriteTarget().getType())
            {
                favoutriteTargets.add(tower);
            }
        }
        ArrayList<Integer> manhatanianDistances = new ArrayList<>();
        if (favoutriteTargets.size() != 0)
        {
            for (DefensiveTower favoutriteTarget : favoutriteTargets)
            {
                manhatanianDistances.add(manhatanianDistance (favoutriteTarget.getLocation() , super.getSoldierLocation()));
            }
            Collections.sort(manhatanianDistances);
            for (DefensiveTower favoutriteTarget : favoutriteTargets)
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

    private boolean isntTargetTooFar(DefensiveTower favoutriteTarget)
    {
        return manhatanianDistance(getSoldierLocation() , favoutriteTarget.getLocation()) < 24;//TODO to be manipulated for increasing the performance
    }

    private boolean isTargetReachable(DefensiveTower favoutriteTarget)
    {
        return attack.getMap().isreachable(favoutriteTarget.getLocation());
    }

    private Integer manhatanianDistance(Point location1, Point location2)
    {
        return Math.abs(location1.getX() - location2.getX()) + Math.abs(location1.getY() - location2.getY());
    }

}
