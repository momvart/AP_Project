package models;

import models.buildings.Building;
import models.buildings.Storage;
import models.soldiers.Soldier;

import java.util.ArrayList;
import java.util.List;

public class Village
{
    private Map map;
    private ConstructionManager constructionManager;
    private VillageStatus villageStatus;
    private int turn;
    private ArrayList<Soldier> soldiers;

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
}
