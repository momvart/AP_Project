package models.soldiers;


import models.Attack;

public class Healer extends Soldier
{
    private int timeTillDie = 10;

    public Healer(int level)
    {
        super(level);
    }

    public static final int SOLDIER_TYPE = 6;

    public int getType()
    {
        return 6;
    }

    public void ageOneDeltaT()
    {
        if (timeTillDie > 0)
        {
            timeTillDie--;
        }
    }

    public int getTimeTillDie()
    {
        return timeTillDie;
    }

    @Override
    public void participateIn(Attack attack)
    {
        setAttackHelper(new HealerAttackHelper(attack, this));
    }
}
