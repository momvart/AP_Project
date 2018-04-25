package models;

import models.buildings.*;
import utils.*;

import java.util.ArrayList;

public class AttackMap extends Map
{
    private ArrayList<DefensiveTower> defensiveTowers;

    public AttackMap(Size size)
    {
        super(size);
        defensiveTowers = new ArrayList<>();
    }

    @Override
    public void addBuilding(Building building)
    {
        super.addBuilding(building);
        if (building instanceof DefensiveTower)
            defensiveTowers.add((DefensiveTower)building);
    }

    public ArrayList<DefensiveTower> getDefensiveTowers()
    {
        return defensiveTowers;
    }

    public Building getNearestBuilding(Point location, int BuildingType)
    {
        return null;
    }
}
