package models.buildings;

import utils.Point;

public class ElixirMine extends Mine
{
    public ElixirMine(Point location)
    {
        super(location);
        setResourceAddPerDeltaT(5);
    }

    @Override
    public void mine(Storage storage)
    {

    }

    @Override
    public int getType()
    {
        return 2;
    }
}
