package models.buildings;

import utils.Point;

public abstract class Building
{
    protected int buildingNum;
    protected Point location;
    protected boolean destroyed;
    protected int level;
    protected int strength;
    protected BuildStatus buildStatus;

    public abstract int getType();

    public String getName()
    {
        return BuildingValues.getBuildingInfo(getType()).getName();
    }

    public int getBuildingNum()
    {
        return buildingNum;
    }

    public Point getLocation()
    {
        return location;
    }

    public boolean isDestroyed()
    {
        return destroyed;
    }

    public int getLevel()
    {
        return level;
    }

    public int getStrength()
    {
        return strength;
    }

    public BuildStatus getBuildStatus()
    {
        return buildStatus;
    }

    public void upgrade()
    {
        level++;
    }
}
