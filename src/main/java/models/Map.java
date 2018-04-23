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
    private transient Building[][] map;
    private ArrayList<Building> buildings = new ArrayList<>();
    private Size size;
    private transient ArrayList<Storage> storages = new ArrayList<>();
    private transient ArrayList<DefensiveTower> defensiveTowers = new ArrayList<>();
    private Resource resources = new Resource(0, 0);

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
        Storage elixirStorage = BuildingFactory.createBuildingByType(ElixirStorage.class, new Point(size.getWidth() / 2 + 4, size.getHeight() / 2 + 4));
        buildings.add(elixirStorage);
    }

    public void addBuilding(Building building)
    {
        map[building.getLocation().getX()][building.getLocation().getY()] = building;
        buildings.add(building);
    }

    public ArrayList<Building> getBuildings()
    {
        return buildings;
    }

    /**
     * Checks if the location is in map or not.
     *
     * @param location The location to check
     * @return
     */
    public boolean isValid(Point location)
    {
        return isValid(location.getX(), location.getY());
    }

    /**
     * Checks if the location is in map or not.
     * @param x The coordinate x
     * @param y The coordinate y
     * @return
     */
    public boolean isValid(int x, int y)
    {
        return x <= size.getWidth() && y <= size.getHeight() && x >= 0 && y >= 0;
    }

    /**
     * Checks if location is empty for new building. Marginal cells are not empty.
     *
     * @return
     */
    public boolean isEmpty(Point location) {return isEmpty(location.getX(), location.getY());}

    /**
     * Checks if location is empty for new building. Marginal cells are not empty.
     *
     * @param x
     * @param y
     * @return
     */
    public boolean isEmpty(int x, int y)
    {
        if (x == 0 || y == 0)
            return false;
        if (x == size.getWidth() - 1 || y == size.getHeight() - 1)
            return false;
        return isValid(x, y) && map[x][y] == null;
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

    public <T extends Building> Stream<T> getBuildings(Class<T> buildingType)
    {
        return buildings.stream()
                .filter(buildingType::isInstance)
                .map(building -> (T)building);
    }
    public Stream<Building> getBuildings(int buildingType)
    {
        return buildings.stream().filter(building -> building.getType() == buildingType);
    }

    public Size getSize()
    {
        return size;
    }

    public Resource getResources()
    {
        return resources;
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
