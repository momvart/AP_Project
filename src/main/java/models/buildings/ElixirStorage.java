package models.buildings;

import utils.Point;

public class ElixirStorage extends Storage
{
    private static final int DEFAULT_CAPACITY = 20;
    public ElixirStorage(Point location)
    {
        super(location);
        capacity = DEFAULT_CAPACITY;
    }

    public static final int BUILDING_TYPE = 4;
    @Override
    public int getType()
    {
        return BUILDING_TYPE;
    }
}
