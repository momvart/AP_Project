package models.buildings;

import models.attack.Attack;
import models.attack.attackHelpers.AreaAttackHelper;
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

    @Override
    public void participateIn(Attack attack)
    {
        attackHelper = new AreaAttackHelper(this, attack);
    }
}
