package models.buildings;

import models.Attack;
import utils.Point;

public class ArcherTower extends DefensiveTower
{
    public ArcherTower(Point location, int buildingNum)
    {
        super(location, buildingNum);
    }

    public static final int BUILDING_TYPE = 8;

    @Override
    public int getType()
    {
        return BUILDING_TYPE;
    }
}
