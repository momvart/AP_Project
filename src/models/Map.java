package models;

import models.soldiers.Soldier;
import models.buildings.Building;
import models.buildings.DefenseType;
import models.buildings.TownHall;
import utils.Point;

import java.util.ArrayList;

public class Map
{
    private Building[][] map = new Building[30][30];
    private ArrayList<Building> buildings;

    public Building[][] getMap()
    {
        return map;
    }

    public ArrayList<Building> getBuildings()
    {
        return buildings;
    }

    public boolean isValid(Point location)
    {
        return false;
    }

    public Building getNearestBuilding(Point location, int BuildingType)
    {
        return null;
    }

    public Soldier getNearestSoldier(Point location, DefenseType defenseType)
    {
        return null;
    }

    public TownHall getTownHall()
    {
        return (TownHall)buildings.get(0);
    }

    public ArrayList<Building> getBuildings(int getType)
    {
        ArrayList<Building> buildingArray = new ArrayList<>(getBuildingsCount(getType));
        for (Building building : buildings)
        {
            if (building.getType() == getType)
                buildingArray.add(building);
        }
        return buildingArray;
    }

    public int getBuildingsCount(int getType)
    {
        int count = 0;
        for (Building building : buildings)
        {
            if (building.getType() == getType)
                count++;
        }
        return count;
    }
}
