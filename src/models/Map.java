package models;

import models.soldiers.Soldier;
import models.buildings.Building;
import models.buildings.DefenseType;
import models.buildings.TownHall;
import utils.Point;
import utils.Size;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Map
{
    private Building[][] map;
    private ArrayList<Building> buildings;
    private Size size;

    public Map(Size size)
    {
        this.size = size;
        map = new Building[size.getWidth()][size.getHeight()];
    }

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

    public Size getSize()
    {
        return size;
    }
}
