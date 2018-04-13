package model;

import model.buildings.Building;
import model.buildings.DefenseType;
import utils.Point;

public class Map
{
    Building[][] map = new Building[30][30];

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

}
