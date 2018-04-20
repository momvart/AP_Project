package models.buildings;

import utils.Point;

public class GoldMine extends Mine
{


    public GoldMine(Point location)
    {
        super(location);
        setResourceAddPerDeltaT(10);
    }

    @Override
    public int getType()
    {
        return 1;
    }
}
