package models;

import exceptions.NoAvailableBuilderException;
import exceptions.NotEnoughResourceException;
import models.buildings.*;
import models.soldiers.Soldier;
import utils.Point;
import utils.Size;

import java.util.ArrayList;

public class Village
{
    private Map map = new Map(new Size(30, 30));
    private ConstructionManager constructionManager = new ConstructionManager(this);
    private VillageStatus villageStatus = VillageStatus.NORMAL;
    private int turn = 0;
    private ArrayList<Soldier> soldiers = new ArrayList<>();

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

    public void construct(int buildingType, Point location) throws NotEnoughResourceException, NoAvailableBuilderException
    {

        Resource cost = BuildingValues.getBuildingInfo(buildingType).getBuildCost();
        Resource available  = getResources();
        if(available.isLessThanOrEqual(cost))
            throw new NotEnoughResourceException(available,cost);
        constructionManager.construct(buildingType,location);
        getResources().decrease(cost);

    }
    public void  upgradeBuilding(Building building) throws NotEnoughResourceException, NoAvailableBuilderException
    {
        Resource cost= BuildingValues.getBuildingInfo(building.getType()).getBuildCost();
        Resource available = getResources();
        if(available.isLessThanOrEqual(cost))
            throw new NotEnoughResourceException(available,cost);
        constructionManager.upgrade(building);
        getResources().decrease(cost);

    }
}
