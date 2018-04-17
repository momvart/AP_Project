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
    public void mine(Storage storage)
    {

    }

    @Override
    public int getType()
    {
        return 1;
    }
}
