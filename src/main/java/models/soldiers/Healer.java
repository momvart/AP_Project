package models.soldiers;


import models.Attack;

public class Healer extends Soldier
{
    private int timeTillDie = 10;

    public Healer(int level)
    {
        super(level);
    }


    public int getType()
    {
        return 5;
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
        setAttackHelper(new HealerAttackHelper(attack, this, getLocation(), getDamage(), SoldierValues.getSoldierInfo(this.getType()).getRange()));
    }
}
