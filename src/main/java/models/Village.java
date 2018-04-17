package models;

import models.buildings.*;
import models.soldiers.Soldier;
import utils.Point;
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

}
