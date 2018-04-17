package models.buildings;

import utils.Point;

public class GoldStorage extends Storage
{

    public GoldStorage(Point location)
    {
        super(location);
    }

    @Override
    public int getType()
    {
        return 3;
    }
}
