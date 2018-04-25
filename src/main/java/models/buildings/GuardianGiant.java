package models.buildings;

import models.Attack;
import utils.Point;

public class GuardianGiant extends DefensiveTower
{
    public GuardianGiant(Point location, int buildingNum)
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
        return 14;
    }
}
