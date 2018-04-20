package models;

import models.buildings.*;
import models.soldiers.Soldier;
import utils.Point;
import utils.Size;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Map
{
    private Building[][] map;
    private ArrayList<Building> buildings = new ArrayList<>();
    private Size size;
    private ArrayList<Storage> storages = new ArrayList<>();
    private ArrayList<DefensiveTower> defensiveTowers = new ArrayList<>();

    public Map(Size size)
    {
        this.size = size;
        map = new Building[size.getWidth()][size.getHeight()];
        TownHall townHall = BuildingFactory.createBuildingByType(TownHall.class, new Point(size.getWidth() / 2 - 1, size.getHeight() / 2 - 1));
        map[14][14] = townHall;
        map[14][15] = townHall;
        map[15][14] = townHall;
        map[15][15] = townHall;
        buildings.add(townHall);
        Storage goldStorage = BuildingFactory.createBuildingByType(GoldStorage.class, new Point(size.getWidth() / 2 - 4, size.getHeight() / 2 - 4));
        buildings.add(goldStorage);
        Storage elixirStorage = BuildingFactory.createBuildingByType(ElixirStorage.class,new Point(size.getWidth()/2 + 4 ,size.getHeight()/2 + 4 ));
        buildings.add(elixirStorage);
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
        return isValid(location.getX(), location.getY());
    }

    public boolean isValid(int x, int y)
    {
        //TODO: return correct value
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

    public Stream<Building> getBuildings(int buildingType)
    {
        return buildings.stream()
                .filter(building -> building.getType() == buildingType);
    }

    public Size getSize()
    {
        return size;
    }

    public Resource getResources()
    {
        Resource resource = new Resource(0, 0);
        for (Storage storage : storages)
        {
            if (storage.getType() == 3)
                resource.gold += storage.getCurrentAmount();
            else
                resource.elixir += storage.getCurrentAmount();
        }
        return resource;
    }

    public ArrayList<Storage> getStorages()
    {
        return storages;
    }

    public ArrayList<DefensiveTower> getDefensiveTowers()
    {
        return defensiveTowers;
    }
}
