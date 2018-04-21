package models.buildings;


import utils.Point;

public abstract class Storage extends VillageBuilding
{
    int capacity;

    public Storage(Point location)
    {
        super(location);
    }

    public int getCapacity()
    {
        return capacity;
    }

    @Override
    public void upgrade()
    {
        super.upgrade();
        double newCapacity = capacity * 1.6;
        capacity = (int)newCapacity;
    }

}
