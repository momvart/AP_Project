package models;

import models.buildings.*;
import models.soldiers.Soldier;
import serialization.ClassAdapter;
import utils.MySortedList;
import utils.Point;
import utils.Size;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Map
{
    private transient Building[][] map;
    private transient ArrayList<MySortedList<Long, Building>> buildings;
    private Size size;
    private Resource resources = new Resource(0, 0);

    public Map()
    {

    }

    public Map(Size size)
    {
        this.size = size;

        setUpBuildingsLists(new ArrayList<>());

        initialize();
    }

    public void initialize()
    {
        TownHall townHall = BuildingFactory.createBuildingByType(TownHall.class, new Point(size.getWidth() / 2 - 1, size.getHeight() / 2 - 1), 1);
        map[townHall.getLocation().getX()][townHall.getLocation().getY()] = townHall;
        map[townHall.getLocation().getX()][townHall.getLocation().getY() + 1] = townHall;
        map[townHall.getLocation().getX() + 1][townHall.getLocation().getY()] = townHall;
        map[townHall.getLocation().getX() + 1][townHall.getLocation().getY() + 1] = townHall;
        addBuilding(townHall);
        Storage goldStorage = BuildingFactory.createBuildingByType(GoldStorage.class, new Point(size.getWidth() / 2 - 4, size.getHeight() / 2 - 4), 1);
        addBuilding(goldStorage);
        Storage elixirStorage = BuildingFactory.createBuildingByType(ElixirStorage.class, new Point(size.getWidth() / 2 + 4, size.getHeight() / 2 + 4), 1);
        addBuilding(elixirStorage);
    }

    public Size getSize()
    {
        return size;
    }

    public Resource getResources()
    {
        return resources;
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
     *
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

    public void addBuilding(Building building)
    {
        if (building.getBuildingNum() < 0)
            throw new IllegalArgumentException("Building number is not valid.");
        if (!isValid(building.getLocation()))
            throw new IllegalArgumentException("Location is not valid.");

        map[building.getLocation().getX()][building.getLocation().getY()] = building;
        buildings.get(building.getType() - 1).addValue(building);
    }

    public void setUpBuildingsLists(Iterable<Building> buildingsList)
    {
        buildings = new ArrayList<>(BuildingValues.BUILDING_TYPES_COUNT);
        Function<Building, Long> keyExtractor = Building::getID;
        for (int i = 0; i < BuildingValues.BUILDING_TYPES_COUNT; i++)
            buildings.add(new MySortedList<>(keyExtractor));

        map = new Building[size.getWidth()][size.getHeight()];

        for (Building b : buildingsList)
            addBuilding(b);
    }

    public ArrayList<Building> getBuildings()
    {
        ArrayList<Building> retVal = new ArrayList<>();
        for (MySortedList<Long, Building> list : buildings)
            retVal.addAll(list.getValues());

        return retVal;
    }

    public ArrayList<MySortedList<Long, Building>> getAllBuildings()
    {
        return buildings;
    }

    public MySortedList<Long, Building> getBuildings(int buildingType)
    {
        return buildings.get(buildingType - 1);
    }

    public <T extends Building> void forEachBuilding(Class<T> type, Consumer<T> consumer)
    {
        for (MySortedList<Long, Building> list : buildings)
            if (type.isInstance(list.getMin()))
                list.forEach(b -> consumer.accept(type.cast(b)));
    }

    public TownHall getTownHall()
    {
        return (TownHall)getBuildings(TownHall.BUILDING_TYPE).getByIndex(0);
    }

}
