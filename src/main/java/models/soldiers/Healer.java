package models.soldiers;


import models.attack.Attack;
import models.attack.attackHelpers.HealerAttackHelper;

public class Healer extends Soldier
{
    public Healer(int level)
    {
        super(level);
    }

    public static final int SOLDIER_TYPE = 6;

    public int getType()
    {
        return 6;
    }

    @Override
    public void participateIn(Attack attack)
    {
        attackHelper = new HealerAttackHelper(attack, this);
    }
}
