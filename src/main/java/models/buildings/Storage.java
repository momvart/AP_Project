package models.buildings;


import utils.Point;

public abstract class Storage extends VillageBuilding
{
    int capacity;
    int currentAmount;

    public Storage(Point location)
    {
        super(location);
    }

    public int getCapacity()
    {
        return capacity;
    }

    public int getFreeCapacity()
    {
        return capacity - currentAmount;
    }

    public int getCurrentAmount()
    {
        return currentAmount;
    }

    @Override
    public void upgrade()
    {
        super.upgrade();
        double newCapacity = capacity * 1.6;
        capacity = (int)newCapacity;
    }

    public void addToStorage(int amount)
    {
        currentAmount = (amount + currentAmount) <= capacity ? amount + currentAmount : capacity;
    }
}
