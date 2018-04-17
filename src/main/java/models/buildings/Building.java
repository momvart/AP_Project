package models.buildings;

import models.World;
import utils.Point;

public abstract class Building
{
    protected int buildingNum;
    protected Point location;
    protected boolean destroyed;
    protected int level;
    protected int strength;
    protected BuildStatus buildStatus;

    public Building(Point location)
    {
        this.location = location;
        if(World.sCurrentGame != null)
        this.buildingNum = World.sCurrentGame.getVillage().getBuildings().size() + 1;
        else
            buildingNum = 1;
        this.level = 0;
        this.strength = BuildingValues.getBuildingInfo(getType()).initialStrength;
        this.destroyed = false;
        this.buildStatus = BuildStatus.BUILT;
    }

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

    public BuildingInfo getBuildingInfo()
    {
        return BuildingValues.getBuildingInfo(getType());
    }
}
