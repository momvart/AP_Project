package models.buildings;

import menus.BuildingSubmenu;
import menus.ParentMenu;
import menus.Submenu;
import models.World;
import utils.Point;

import java.util.Comparator;
import java.util.Optional;

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
        if (World.sCurrentGame != null)
        {
            World.sCurrentGame.getVillage().getMap().getBuildings(getType())
                    .max(Comparator.comparingInt(Building::getBuildingNum))
                    .ifPresent(building -> {
                        if (building.buildingNum == 0)
                            building.buildingNum = 1;
                        this.buildingNum = building.buildingNum + 1;
                    });
        }
        else
            buildingNum = 0;
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

    public Submenu getMenu(ParentMenu parent)
    {
        return new BuildingSubmenu(parent, this);
    }
}
