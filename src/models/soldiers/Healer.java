package models.soldiers;


import models.Attack;

public class Healer extends Soldier
{
    private int timeTillDie = 10;

    public Healer() {super();}

    public int getType()
    {
        return 5;
    }

    public int getHealingAmount()
    {
        return SoldierValues.getSoldierInfo(this.getType()).getInitialDamage();//TODO I'm teling that damage of healer should be her healing amount
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
        super.setAttackHelper(new HealerAttackHelper(attack));
    }
}
