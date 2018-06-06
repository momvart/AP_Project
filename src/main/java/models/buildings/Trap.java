package models.buildings;

import utils.Point;

public class Trap extends DefensiveTower
{
    public Trap(Point location, int buildingNum)
    {
        super(location, buildingNum);
    }

    @Override
    public int getType()
    {
        return 13;
    }
}
