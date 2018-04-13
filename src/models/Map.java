package models;

import models.soldiers.Soldier;
import models.buildings.Building;
import models.buildings.DefenseType;
import models.buildings.TownHall;
import utils.Point;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    public List<Building> getBuildings(int buildingType)
    {
        return buildings.stream()
                .filter(building -> building.getType() == buildingType)
                .collect(Collectors.toList());
    }
}
