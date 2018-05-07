package models;

import exceptions.FilledCellException;
import models.buildings.*;
import utils.MySortedList;
import utils.Point;
import utils.Size;

import java.util.ArrayList;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

public class Map
{
    private transient Building[][] map;
    private transient ArrayList<MySortedList<Long, Building>> buildings;
    private Size size;
    protected Resource resources = new Resource(0, 0);

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
        addBuilding(townHall);
        map[townHall.getLocation().getX()][townHall.getLocation().getY() + 1] = townHall;
        map[townHall.getLocation().getX() + 1][townHall.getLocation().getY()] = townHall;
        map[townHall.getLocation().getX() + 1][townHall.getLocation().getY() + 1] = townHall;
        Storage goldStorage = BuildingFactory.createBuildingByType(GoldStorage.class, new Point(size.getWidth() / 2 - 4, size.getHeight() / 2 - 4), 1);
        addBuilding(goldStorage);
        Storage elixirStorage = BuildingFactory.createBuildingByType(ElixirStorage.class, new Point(size.getWidth() / 2 + 4, size.getHeight() / 2 + 4), 1);
        addBuilding(elixirStorage);
    }

    public Size getSize()
    {
        return size;
    }

    public int getWidth() {return size.getWidth();}

    public int getHeight() {return size.getHeight();}

    public Resource getResources()
    {
        return resources;
    }

    ///region Checking Cells

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
        return x < size.getWidth() && y < size.getHeight() && x >= 0 && y >= 0;
    }

    /**
     * Checks if location is empty for new building. Marginal cells are not empty. Zero based.
     *
     * @return
     */
    public boolean isEmptyForBuilding(Point location) {return isEmptyForBuilding(location.getX(), location.getY());}

    /**
     * Checks if location is empty for new building. Marginal cells are not empty. Zero based.
     *
     * @param x
     * @param y
     * @return
     */
    public boolean isEmptyForBuilding(int x, int y)
    {
        return !isMarginal(x, y) && isEmpty(x, y);
    }

    public boolean isEmpty(int x, int y)
    {
        return isValid(x, y) && map[x][y] == null;
    }

    public boolean isMarginal(int x, int y)
    {
        return x == 0 || y == 0 || x == size.getWidth() - 1 || y == size.getHeight() - 1;
    }

    public boolean isMarginal(Point location)
    {
        return isMarginal(location.getX(), location.getY());
    }

    ///endregion

    ///region Buildings

    public void addBuilding(Building building) throws FilledCellException
    {
        if (building.getBuildingNum() < 0)//TODO‌ NullpointerException is being thrown in here
            throw new IllegalArgumentException("Building number is not valid.");
        if (!isValid(building.getLocation()))
            throw new IllegalArgumentException("Location is not valid.");
        if (!isEmptyForBuilding(building.getLocation()))
            throw new FilledCellException(building.getLocation());

        map[building.getLocation().getX()][building.getLocation().getY()] = building;
        buildings.get(building.getType() - 1).addValue(building);
    }

    public void setUpBuildingsLists(Iterable<Building> buildingsList)
    {
        buildings = new ArrayList<>(BuildingValues.BUILDING_TYPES_COUNT);
        Function<Building, Long> keyExtractor = Building::getId;
        for (int i = 0; i < BuildingValues.BUILDING_TYPES_COUNT; i++)
            buildings.add(new MySortedList<>(keyExtractor));

        map = new Building[size.getWidth()][size.getHeight()];

        buildingsList.forEach(this::addBuilding);
    }

    public Building getBuildingAt(int x, int y)
    {
        return map[x][y];
    }

    public Building getBuildingAt(Point location)
    {
        return getBuildingAt(location.getX(), location.getY());
    }

    public ArrayList<Building> getBuildings()
    {
        ArrayList<Building> retVal = new ArrayList<>();
        for (MySortedList<Long, Building> list : buildings)
            retVal.addAll(list.getValues());

        return retVal;
    }

    public MySortedList<Long, Building> getBuildings(int buildingType)
    {
        return buildings.get(buildingType - 1);
    }

    public Stream<Building> getAllBuildings()
    {
        return buildings.stream().flatMap(list -> list.getValues().stream());
    }

    public <T extends Building> void forEachBuilding(Class<T> type, Consumer<T> consumer)
    {
        for (MySortedList<Long, Building> list : buildings)
            if (type.isInstance(list.getMin()))
                list.forEach(b -> consumer.accept(type.cast(b)));
    }

    public <T extends Building> T getBuildingById(long id)
    {
        return (T)getBuildings((int)(id >>> Integer.SIZE)).get(id);
    }

    public TownHall getTownHall()
    {
        return (TownHall)getBuildings(TownHall.BUILDING_TYPE).getByIndex(0);
    }

    ///endregion
}
