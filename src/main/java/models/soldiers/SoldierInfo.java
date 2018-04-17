package models.soldiers;

import models.Resource;
import models.buildings.Building;

public class SoldierInfo
{
    private String name;
    private Resource brewCost;
    private int brewTime;
    private int speed;
    private int range;
    private Building favouriteTarget;
    private int initialHealth;
    private int initialDamage;

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

    public Building getFavouriteTarget()
    {
        return favouriteTarget;
    }

    public int getInitialHealth()
    {
        return initialHealth;
    }

    public int getInitialDamage()
    {
        return initialDamage;
    }
}
