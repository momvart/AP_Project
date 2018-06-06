package models.buildings;

import utils.Point;

public class Cannon extends DefensiveTower
{
    public static final int DEFENSIVE_TOWER_TYPE = 9;
    private static final int SECOND_RANGE = 2;

    public Cannon(Point location, int buildingNum)
    {
        super(location, buildingNum);
    }

    @Override
    public int getType()
    {
        return 9;
    }
}
