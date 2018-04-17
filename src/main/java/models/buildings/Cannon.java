package models.buildings;

import models.Attack;
import utils.Point;

public class Cannon extends DefensiveTower
{
    public Cannon(Point location)
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
        return 9;
    }
}
