package models.buildings;

import utils.Point;

public abstract class Mine extends VillageBuilding
{
    int resourceAddPerDeltaT;

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
        resourceAddPerDeltaT = (int) newResourceAdd;
    }

    public abstract void mine(Storage storage);

    protected void setResourceAddPerDeltaT(int resourceAddPerDeltaT)
    {
        this.resourceAddPerDeltaT = resourceAddPerDeltaT;
    }
}
