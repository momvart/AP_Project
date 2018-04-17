package models.buildings;

import models.Attack;
import utils.Point;

public class AirDefense extends DefensiveTower
{

    public AirDefense(Point location)
    {
        super(location);
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
