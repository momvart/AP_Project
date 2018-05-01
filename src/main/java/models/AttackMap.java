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

    public List<DefensiveTower> getAllDefensiveTowers()
    {
        return defensiveTowers.getValues();
    }

    public List<DefensiveTower> getDefensiveTowers(int towerType)
    {
        return defensiveTowers.getRange((long)towerType << Integer.SIZE, (e1, e2) ->
        {
            if (e1 >>> Integer.SIZE == e2 >>> Integer.SIZE)
                return 0;
            return e1.compareTo(e2);
        }).getValues();
    }

    public Building getNearestBuilding(Point location, int BuildingType)
    {
        return null;
    }
}
