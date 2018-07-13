package models.buildings;

import models.attack.Attack;
import models.attack.attackHelpers.GuardianGiantAttackHelper;
import utils.Point;

public class GuardianGiant extends DefensiveTower
{
    public static final int GUARDIAN_GIANT_SPEED = 2;

    public GuardianGiant(Point location, int buildingNum)
    {
        super(location, buildingNum);
    }

    @Override
    public int getType()
    {
        return 14;
    }

    @Override
    public void participateIn(Attack attack)
    {
        attackHelper = new GuardianGiantAttackHelper(this, attack);
    }
}
