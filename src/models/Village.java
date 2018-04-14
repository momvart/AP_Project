package models;

import models.buildings.Building;
import models.buildings.Storage;
import models.soldiers.Soldier;

import java.util.ArrayList;
import java.util.List;

public class Village
{
    private Map map;
    private String owner;
    private ConstructionManager constructionManager;
    private VillageStatus villageStatus;
    private int turn;
    private ArrayList<Soldier> soldiers;


    public String getOwner()
    {
        return owner;
    }

    public void setOwner(String owner)
    {
        this.owner = owner;
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
