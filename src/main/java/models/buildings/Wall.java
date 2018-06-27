package models.buildings;

import utils.Point;

public class Wall extends VillageBuilding
{

    public Wall(Point location, int buildingNum)
    {
        super(location, buildingNum);
    }

    public static final int BUILDING_TYPE = 12;

    @Override
    public int getType()
    {
        return BUILDING_TYPE;
    }
}
