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
        Resource free = Resource.subtract(World.getVillage().getTotalResourceCapacity(), World.getVillage().getResources());
        int toSend = Math.min(free.getElixir(), minedResources);
        World.getVillage().getResources().increase(new Resource(0, toSend));
        minedResources -= toSend;
    }

    public static final int BUILDING_TYPE = 2;

    @Override
    public int getType()
    {
        return BUILDING_TYPE;
    }
}
