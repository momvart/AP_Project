package models.buildings;

import models.attack.Attack;
import models.attack.attackHelpers.TrapAttackHelper;
import utils.Point;

public class Trap extends DefensiveTower
{
    public static final int BUILDING_TYPE = 13;
    public Trap(Point location, int buildingNum)
    {
        super(location, buildingNum);
    }

    @Override
    public int getType()
    {
        return 13;
    }

    @Override
    public void participateIn(Attack attack)
    {
        attackHelper = new TrapAttackHelper(this, attack);
    }
}
