package models;

import exceptions.*;
import models.buildings.*;
import models.soldiers.*;
import utils.*;

import java.util.*;
import java.util.stream.*;

public class Village
{
    private Map map;
    private ConstructionManager constructionManager;
    private VillageStatus villageStatus = VillageStatus.NORMAL;
    private int turn = 0;
    private SoldierCollection soldiers;

    public void initialize()
    {
        map = new Map(new Size(30, 30));
        constructionManager = new ConstructionManager();
        soldiers = new SoldierCollection();
    }

    public VillageStatus getVillageStatus()
    {
        return villageStatus;
    }

    public void setVillageStatus(VillageStatus villageStatus)
    {
        this.villageStatus = villageStatus;
    }

    public Map getMap()
    {
        return map;
    }

    public ArrayList<Building> getBuildings()
    {
        return map.getBuildings();
    }

    ///region Resources
    public Resource getResources()
    {
        return map.getResources();
    }

    public Resource getTotalResourceCapacity()
    {
        int gold = 0, elixir = 0;
        for (Building storage : map.getBuildings(GoldStorage.BUILDING_TYPE))
            gold += ((GoldStorage)storage).getCapacity();
        for (Building storage : map.getBuildings(ElixirStorage.BUILDING_TYPE))
            elixir += ((ElixirStorage)storage).getCapacity();
        return new Resource(gold, elixir);
    }

    public void spendResource(Resource toSpend) throws NotEnoughResourceException
    {
        try
        {
            getResources().decrease(toSpend);
        }
        catch (IllegalArgumentException ex)
        {
            throw new NotEnoughResourceException(getResources(), toSpend);
        }
    }
    ///endregion

    ///region Constructions

    public ConstructionManager getConstructionManager()
    {
        return constructionManager;
    }

    public Builder getAvailableBuilder() throws NoAvailableBuilderException
    {
        return map.getTownHall().getAvailableBuilder();
    }

    public void construct(int buildingType, Point location) throws NotEnoughResourceException, NoAvailableBuilderException
    {
        Resource cost = BuildingValues.getBuildingInfo(buildingType).getBuildCost();
        Resource available = getResources();
        if (available.isLessThanOrEqual(cost))
            throw new NotEnoughResourceException(available, cost);
        constructionManager.construct(buildingType, location);
        spendResource(cost);
    }

    public void upgradeBuilding(Building building) throws NotEnoughResourceException, NoAvailableBuilderException
    {
        Resource cost = BuildingValues.getBuildingInfo(building.getType()).getBuildCost();
        Resource available = getResources();
        if (available.isLessThanOrEqual(cost))
            throw new NotEnoughResourceException(available, cost);
        constructionManager.upgrade(building);
        spendResource(cost);
    }

    ///endregion

    public int getTurn()
    {
        return turn;
    }

    public void passTurn()
    {
        if (villageStatus.equals(VillageStatus.NORMAL))
        {
            turn++;
            getMap().forEachBuilding(Mine.class, Mine::passTurn);
            getMap().forEachBuilding(Barracks.class, Barracks::passTurn);
            getMap().getTownHall().passTurn();
            constructionManager.checkConstructions();
        }
    }

    public void setupSoldiers(Iterable<Soldier> soldierList)
    {
        soldiers = new SoldierCollection();
        soldierList.forEach(this::addSoldier);
    }

    public void addSoldier(Soldier soldier) throws NotEnoughCampCapacityException
    {
        if (getCampsCapacity() <= getSoldiersCount())
            throw new NotEnoughCampCapacityException();
        soldiers.addSoldier(soldier);
    }

    public ArrayList<Soldier> getSoldiers(int soldierType)
    {
        return soldiers.getSoldiers(soldierType);
    }

    public Stream<Soldier> getAllSoldiers()
    {
        return soldiers.getAllSoldiers();
    }

    public List<Soldier> selectUnit(int soldierType, int count) throws NotEnoughSoldierException
    {
        int size = getSoldiers(soldierType).size();
        if (size < count)
            throw new NotEnoughSoldierException(soldierType, size, count);
        if (count == -1)
            return getSoldiers(soldierType);
        else
            return getSoldiers(soldierType).subList(0, count);
    }

    public int getCampsCapacity()
    {
        int sum = 0;
        for (Building camp : getMap().getBuildings(Camp.BUILDING_TYPE).getValues())
            sum += ((Camp)camp).getCapacity();
        return sum;
    }

    public int getSoldiersCount()
    {
        return (int)getAllSoldiers().count();
    }
}
