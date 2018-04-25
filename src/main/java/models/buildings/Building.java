package models.buildings;

import menus.BuildingSubmenu;
import menus.ParentMenu;
import utils.Point;

public abstract class Building
{
    private int buildingNum = -1;
    protected Point location;
    protected boolean destroyed = false;
    protected int level;
    protected int strength;
    protected BuildStatus buildStatus = BuildStatus.BUILT;

    public Building()
    {

    }

    public Building(Point location, int buildingNum)
    {
        this.location = location;
        this.buildingNum = buildingNum;
        this.strength = BuildingValues.getBuildingInfo(getType()).initialStrength;
    }

    public long getId()
    {
        return getType() << (Integer.SIZE / Byte.SIZE) + buildingNum;
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

    public void setBuildStatus(BuildStatus buildStatus)
    {
        this.buildStatus = buildStatus;
    }

    public void upgrade()
    {
        level++;
    }

    public void decreaseStrength(int amount)
    {
        this.strength -= amount;
    }

    public BuildingInfo getBuildingInfo()
    {
        return BuildingValues.getBuildingInfo(getType());
    }

    public BuildingSubmenu getMenu(ParentMenu parent)
    {
        return new BuildingSubmenu(parent, this);
    }
}
