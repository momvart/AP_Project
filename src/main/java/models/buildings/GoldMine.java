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
        World.getVillage().getResources().increase(new Resource(minedResources, 0));
        minedResources = 0;
    }

    public static final int BUILDING_TYPE = 1;

    @Override
    public int getType()
    {
        return BUILDING_TYPE;
    }
}
