package models.buildings;

import graphics.drawers.BuildingDrawer;
import graphics.drawers.WallDrawer;
import models.Map;
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

    @Override
    public BuildingDrawer createGraphicDrawer(Map container)
    {
        return new WallDrawer(this, container);
    }
}
