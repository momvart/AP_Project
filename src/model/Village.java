package model;

import model.buildings.Building;
import model.buildings.TownHall;

import java.util.ArrayList;

public class Village
{
    private Map map;
    private String owner;
    private ConstructionManager constructionManager;
    private VillageStatus villageStatus;
    private int turn;
    private ArrayList<Building> buildings;
    private ArrayList<Soldier> soldiers;

    public ArrayList<Building> getBuildings()
    {
        return buildings;
    }

    public ArrayList<Soldier> getSoldiers()
    {
        return soldiers;
    }

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

    public TownHall getTownHall()
    {
        return (TownHall)buildings.get(1);
    }
}
