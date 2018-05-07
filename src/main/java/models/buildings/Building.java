package models.buildings;

import exceptions.UnavailableUpgradeException;
import menus.BuildingInfoSubmenu;
import menus.BuildingSubmenu;
import menus.ParentMenu;
import utils.Point;

public abstract class Building
{
    private int buildingNum = -1;
    protected Point location;
    protected transient boolean destroyed = false;
    protected int level;
    protected transient int strength;
    protected BuildStatus buildStatus = BuildStatus.BUILT;

    public Building()
    {
        ensureLevel();
    }

    public Building(Point location, int buildingNum)
    {
        this();
        this.location = location;
        this.buildingNum = buildingNum;
    }

    public long getId()
    {
        return ((long)getType() << Integer.SIZE) + buildingNum;
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

    /**
     * CAUTION: Don't change building num unless if you are deserializing or data is lost.
     *
     * @param buildingNum
     */
    public void setBuildingNum(int buildingNum)
    {
        this.buildingNum = buildingNum;
    }

    public Point getLocation()
    {
        return location;
    }

    public void setLocation(Point location)
    {
        this.location = location;
    }

    public boolean isDestroyed()
    {
        return destroyed;
    }

    public void setDestroyed(boolean destroyed)
    {
        this.destroyed = destroyed;
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

    public void upgrade() throws UnavailableUpgradeException
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


    /**
     * Method for setting properties of this building by level
     */
    public void ensureLevel()
    {
        strength = getBuildingInfo().getInitialStrength() + getBuildingInfo().getUpgradeStrengthInc() * level;
    }

    public BuildingInfoSubmenu getInfoSubmenu() { return new BuildingInfoSubmenu(null); }

    public BuildingSubmenu getMenu(ParentMenu parent)
    {
        return new BuildingSubmenu(parent, this, getInfoSubmenu());
    }
}
