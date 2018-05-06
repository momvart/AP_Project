package models.soldiers;

import models.Resource;
import models.buildings.Building;

public class SoldierInfo
{

    private int type;
    private String name;
    private Resource brewCost;
    private int brewTime;
    private int speed;
    private int range;
    private Class<? extends Building>[] favouriteTargets;
    private int initialHealth;
    private int initialDamage;
    private int minBarracksLevel;
    private MoveType moveType;

    public SoldierInfo()
    {

    }

    public int getType()
    {
        return type;
    }

    public Class getSoldierClass()
    {
        return SoldierValues.sSoldierClasses[type - 1];
    }

    public String getName()
    {
        return name;
    }

    public Resource getBrewCost()
    {
        return brewCost;
    }

    public int getBrewTime()
    {
        return brewTime;
    }

    public int getSpeed()
    {
        return speed;
    }

    public int getRange()
    {
        return range;
    }

    public Class<? extends Building>[] getFavouriteTargets()
    {
        return favouriteTargets;
    }

    public int getInitialHealth()
    {
        return initialHealth;
    }

    public int getInitialDamage()
    {
        return initialDamage;
    }

    public MoveType getMoveType()
    {
        return moveType;
    }

    public int getMinBarracksLevel()
    {
        return minBarracksLevel;
    }
}
