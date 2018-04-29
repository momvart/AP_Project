package models.soldiers;

import models.Builder;
import models.Resource;
import models.buildings.Building;
import models.buildings.Wall;

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
    private int minBarracksLevel;

    public SoldierInfo()
    {

    }

    public SoldierInfo(int type, String name, Resource brewCost, int brewTime, int speed, int range, int initialHealth, int initialDamage, int minBarracksLevel, Class<? extends Building>... favouriteTargets)
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
        this.minBarracksLevel = minBarracksLevel;
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

    public int getMinBarracksLevel()
    {
        return minBarracksLevel;
    }
}
