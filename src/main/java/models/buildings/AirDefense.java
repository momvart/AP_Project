package models.buildings;

import utils.Point;

public class AirDefense extends DefensiveTower
{

    public AirDefense(Point location, int buildingNum)
    {
        super(location, buildingNum);
    }

    @Override
    public int getType()
    {
        return 10;
    }
}
