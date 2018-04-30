package models;

import models.buildings.*;
import utils.*;

import java.util.*;

public class AttackMap extends Map
{
    private MySortedList<Long, DefensiveTower> defensiveTowers;

    public AttackMap(Size size)
    {
        super(size);
        defensiveTowers = new MySortedList<>(DefensiveTower::getId);
    }

    public AttackMap(Size size, Resource resource)
    {
        this(size);
        this.resources = resource;
    }

    @Override
    public void addBuilding(Building building)
    {
        super.addBuilding(building);
        if (building instanceof DefensiveTower)
            defensiveTowers.addValue((DefensiveTower)building);
    }

    public List<DefensiveTower> getDefensiveTowers()
    {
        return defensiveTowers.getValues();
    }

    public Building getNearestBuilding(Point location, int BuildingType)
    {
        return null;
    }
}
