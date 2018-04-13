package models.buildings;


public abstract class Storage extends VillageBuilding
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
    public void upgrade()
    {
        super.upgrade();
        double newCapacity = capacity * 1.6;
        capacity = (int) newCapacity;
    }
}
