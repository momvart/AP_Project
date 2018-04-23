package models.soldiers;

import models.Builder;
import models.Resource;
import models.buildings.Building;

import java.lang.reflect.Type;

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

    public SoldierInfo()
    {

    }

    public SoldierInfo(int type, String name, Resource brewCost, int brewTime, int speed, int range, int initialHealth, int initialDamage, Class<? extends Building>... favouriteTargets)
    {
        this.type = type;
        this.name = name;
        this.brewCost = brewCost;
        this.brewTime = brewTime;
        this.speed = speed;
        this.range = range;
        this.favouriteTargets = favouriteTargets;
        this.initialHealth = initialHealth;
        this.initialDamage = initialDamage;
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
}
