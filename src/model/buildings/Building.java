package model.buildings;

import utils.Point;

public abstract class Building
{
    int buildingNum;
    Point location;
    boolean destroyed;
    int level;
    int strength;
    BuildStatus buildStatus;

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

    public abstract int getType();
}
