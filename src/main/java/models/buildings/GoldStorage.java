package models.buildings;

import utils.Point;

public class GoldStorage extends Storage
{
    private static final int DEFAULT_CAPACITY = 500;
    public GoldStorage(Point location)
    {
        super(location);
        capacity = DEFAULT_CAPACITY;
    }

    @Override
    public int getType()
    {
        return 3;
    }
}
