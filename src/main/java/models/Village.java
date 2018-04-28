package models;

import exceptions.NoAvailableBuilderException;
import exceptions.NotEnoughResourceException;
import models.buildings.*;
import models.soldiers.Soldier;
import utils.Point;
import utils.Size;

import java.util.ArrayList;
import java.util.stream.Stream;

public class Village
{
    private Map map;
    private ConstructionManager constructionManager;
    private VillageStatus villageStatus = VillageStatus.NORMAL;
    private int turn = 0;
    private ArrayList<Soldier> soldiers;

    public void initialize()
    {
        map = new Map(new Size(30, 30));
        constructionManager = new ConstructionManager();
        soldiers = new ArrayList<>();
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

    public ConstructionManager getConstructionManager()
    {
        return constructionManager;
    }

    public int getTurn()
    {
        return turn;
    }

    public ArrayList<Soldier> getSoldiers()
    {
        return soldiers;
    }

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
}
