package models.buildings;

import models.soldiers.Soldier;
import utils.Point;

import java.util.ArrayList;

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

    @Override
    public int getType()
    {
        return 7;
    }

    @Override
    public void upgrade()
    {
        // TODO: 4/13/18 method should throw an exception  
    }
}