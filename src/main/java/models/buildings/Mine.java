package models.buildings;

import models.World;
import utils.Point;

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

    public void passTurn(Storage storage)
    {
        minedResources += resourceAddPerDeltaT;
    }

    protected void setResourceAddPerDeltaT(int resourceAddPerDeltaT)
    {
        this.resourceAddPerDeltaT = resourceAddPerDeltaT;
    }

    protected void mine()
    {
        Storage storage = (Storage)World.sCurrentGame.getVillage().getMap().getBuildings(getType() + 2);
        storage.addToStorage(minedResources);
        minedResources = 0 ;
    }
}
