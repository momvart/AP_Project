package models.buildings;

import models.World;
import utils.Point;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.stream.Stream;

public abstract class Mine extends VillageBuilding
{
    int resourceAddPerDeltaT;
    int minedResources;

    public Mine(Point location)
    {
        super(location);
    }

    public int getResourceAddPerDeltaT()
    {
        return resourceAddPerDeltaT;
    }

    @Override
    public void upgrade()
    {
        super.upgrade();
        double newResourceAdd = resourceAddPerDeltaT * 1.6;
        resourceAddPerDeltaT = (int)newResourceAdd;
    }

    public void passTurn()
    {
        minedResources += resourceAddPerDeltaT;
    }

    protected void setResourceAddPerDeltaT(int resourceAddPerDeltaT)
    {
        this.resourceAddPerDeltaT = resourceAddPerDeltaT;
    }

    public  void mine()
    {
        ArrayList<Storage> storages = World.sCurrentGame.getVillage().getMap().getStorages();
        storages.sort(Comparator.comparingInt(Storage::getFreeCapacity).reversed());
        storages.get(0).addToStorage(minedResources);
        minedResources = 0 ;
    }

    public int getMinedResources()
    {
        return minedResources;
    }
}
