package model;

import model.Soldiers.Soldier;
import model.buildings.Building;
import model.buildings.DefenseType;
import model.buildings.TownHall;
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

    public ArrayList<Soldier> getSoldiers()
    {
        return soldiers;
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
}
