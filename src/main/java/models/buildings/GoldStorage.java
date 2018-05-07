package models.buildings;

import utils.Point;

public class GoldStorage extends Storage
{
    public GoldStorage(Point location, int buildingNum)
    {
        super(location, buildingNum);
        ensureLevel();
    }

    public static final int BUILDING_TYPE = 3;

    @Override
    public int getType()
    {
        return BUILDING_TYPE;
    }
}
