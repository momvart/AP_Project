package model.buildings;


public class Storage extends VillageBuilding
{
    int capacity;
    int currentAmount;

    public int getCapacity()
    {
        return capacity;
    }

    public int getFreeCapacity()
    {
        return currentAmount;
    }

    @Override
    public void destroy()
    {

    }

    @Override
    public int getType()
    {
        return 0;
    }

    @Override
    public void upgrade()
    {
        super.upgrade();
    }
}
