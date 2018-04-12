package model.buildings;

public abstract class Building
{
    int buildingNum;
    Point location;
    boolean destroyed;
    int level;
    int strength;
    EBuildStatus buildStatus;

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

    public EBuildStatus getBuildStatus()
    {
        return buildStatus;
    }

    public void upgrade()
    {
        level++;
    }

    public abstract void destroy();

    public abstract int getType();
}
