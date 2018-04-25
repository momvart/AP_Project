package models.buildings;

import models.Attack;
import utils.Point;

public class AirDefense extends DefensiveTower
{

    public AirDefense(Point location, int buildingNum)
    {
        super(location, buildingNum);
    }

    @Override
    public void attack(Attack attack)
    {

    }

    @Override
    public int getType()
    {
        return 10;
    }
}
