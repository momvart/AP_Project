package models.buildings;

import models.Resource;

public abstract class BuildingInfo
{
    int type;
    String name;
    Resource buildCost;
    int buildDuration;
    int destroyScore;
    Resource destroyResource;
    int initialStrength;

    public BuildingInfo(int type, String name, Resource buildCost, int buildDuration, int destroyScore, Resource destroyResource, int initialStrength)
    {
        this.type = type;
        this.name = name;
        this.buildCost = buildCost;
        this.buildDuration = buildDuration;
        this.destroyScore = destroyScore;
        this.destroyResource = destroyResource;
        this.initialStrength = initialStrength;
    }

    public int getType()
    {
        return type;
    }

    public String getName()
    {
        return name;
    }

    public Resource getBuildCost()
    {
        return buildCost;
    }

    public Resource getUpgradeCost() {return getBuildCost();}

    public int getBuildDuration()
    {
        return buildDuration;
    }

    public int getDestroyScore()
    {
        return destroyScore;
    }

    public Resource getDestroyResource()
    {
        return destroyResource;
    }

    public int getInitialStrength()
    {
        return initialStrength;
    }
}
