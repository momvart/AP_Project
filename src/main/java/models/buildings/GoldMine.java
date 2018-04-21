package models.buildings;

import models.Resource;
import models.Village;
import models.World;
import utils.Point;

public class GoldMine extends Mine
{
    public GoldMine(Point location)
    {
        super(location);
        setResourceAddPerDeltaT(10);
    }

    @Override
    public void mine()
    {
        World.getVillage().getResources().increase(new Resource(minedResources, 0));
        minedResources = 0;
    }

    @Override
    public int getType()
    {
        return 1;
    }
}
