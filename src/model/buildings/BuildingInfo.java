package model.buildings;

public abstract class BuildingInfo
{
    int type;
    String name;
    Resource buildCost;
    int buildDuration;
    int destroyScore;
    Resource destroyResource;

    public BuildingInfo(int type, String name, Resource buildCost, int buildDuration, int destroyScore, Resource destroyResource)
    {
        this.type = type;
        this.name = name;
        this.buildCost = buildCost;
        this.buildDuration = buildDuration;
        this.destroyScore = destroyScore;
        this.destroyResource = destroyResource;
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
}
