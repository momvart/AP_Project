package models.buildings;

import models.Attack;
import utils.Point;

public class Trap extends DefensiveTower
{
    public Trap(Point location, int buildingNum)
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
        return 13;
    }
}
