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
    }

    public abstract void mine(Storage storage);
}
