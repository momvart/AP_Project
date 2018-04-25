package models.buildings;

import models.Resource;
import models.World;
import utils.Point;

public class ElixirMine extends Mine
{
    public ElixirMine(Point location, int buildingNum)
    {
        super(location, buildingNum);
        setResourceAddPerDeltaT(5);
    }

    @Override
    public void mine()
    {
        World.getVillage().getResources().increase(new Resource( 0,minedResources));
        minedResources = 0;
    }

    public static final int BUILDING_TYPE = 2;
    @Override
    public int getType()
    {
        return BUILDING_TYPE;
    }
}
