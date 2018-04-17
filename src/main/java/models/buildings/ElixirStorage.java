package models.buildings;

import utils.Point;

public class ElixirStorage extends Storage
{
    public ElixirStorage(Point location)
    {
        super(location);
    }

    @Override
    public int getType()
    {
        return 4;
    }
}
