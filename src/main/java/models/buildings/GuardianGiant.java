package models.buildings;

import models.Attack;
import utils.Point;

public class GuardianGiant extends DefensiveTower
{
    public GuardianGiant(Point location)
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
        return 14;
    }
}
