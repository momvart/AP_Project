package models.buildings;

import exceptions.UnavailableUpgradeException;
import utils.Point;

public class Camp extends VillageBuilding
{
    private int capacity;

    public Camp(Point location, int buildingNum)
    {
        super(location, buildingNum);
    }


    public int getCapacity()
    {
        return capacity;
    }

    public static final int BUILDING_TYPE = 7;

    @Override
    public int getType()
    {
        return BUILDING_TYPE;
    }

    @Override
    public void upgrade()
    {
        throw new UnavailableUpgradeException(this);
    }
}