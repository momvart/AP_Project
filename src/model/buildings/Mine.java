package model.buildings;

public abstract class Mine extends VillageBuilding
{
    int resourceAddPerDeltaT;

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
}
