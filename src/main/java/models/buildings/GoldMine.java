package models.buildings;

import models.Resource;
import models.Village;
import models.World;
import utils.Point;

public class GoldMine extends Mine
{
    public GoldMine(Point location, int buildingNum)
    {
        super(location, buildingNum);
        setResourceAddPerDeltaT(10);
    }

    @Override
    public void mine()
    {
        Resource free = Resource.subtract(World.getVillage().getTotalResourceCapacity(), World.getVillage().getResources());
        int toSend = Math.min(free.getGold(), minedResources);
        World.getVillage().getResources().increase(new Resource(toSend, 0));
        minedResources -= toSend;
    }

    public static final int BUILDING_TYPE = 1;

    @Override
    public int getType()
    {
        return BUILDING_TYPE;
    }
}
