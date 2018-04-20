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

    @Override
    public int getType()
    {
        return 4;
    }
}
