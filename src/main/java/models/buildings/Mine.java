package models.buildings;

import models.World;
import utils.Point;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.stream.Stream;

public abstract class Mine extends VillageBuilding
{
    private static final String RESOURCE_ADDPDT = "RAPDT";

    protected int resourceAddPerDeltaT;
    protected int minedResources;

    public Mine(Point location, int buildingNum)
    {
        super(location, buildingNum);
    }

    public int getResourceAddPerDeltaT()
    {
        return resourceAddPerDeltaT;
    }

    @Override
    public void ensureLevel()
    {
        super.ensureLevel();
        resourceAddPerDeltaT = (int)(Math.pow(1.6, level) * (double)getBuildingInfo().getMetadata(RESOURCE_ADDPDT));
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

    public abstract void mine();

    public int getMinedResources()
    {
        return minedResources;
    }
}
