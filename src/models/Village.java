package models;

import models.buildings.*;
import models.soldiers.Soldier;
import utils.Size;

import java.util.ArrayList;
import java.util.List;

public class Village
{
    private Map map = new Map(new Size(30, 30));
    private ConstructionManager constructionManager = new ConstructionManager();
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

    public Building getNewBuilding(int type)
    {
        VillageBuilding villageBuilding;
        switch (type)
        {
            case 1:
                villageBuilding = new GoldMine();
                break;
            case 2:
                villageBuilding = new ElixirMine();
                break;
            case 3:
                villageBuilding = new GoldStorage();
                break;
            case 4:
                villageBuilding = new ElixirStorage();
                break;
            case 5:
                villageBuilding = new TownHall();
                break;
            case 6:
                villageBuilding = new Barracks();
                break;
            case 7:
                villageBuilding = new Camp();
                break;
        }
    }
}
