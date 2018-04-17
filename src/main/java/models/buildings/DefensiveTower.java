package models.buildings;

import models.Attack;

public abstract class DefensiveTower extends Building
{
    private int attackPower;
    private int range;

    public int getRange()
    {
        return range;
    }

    public int getAttackPower()
    {
        return attackPower;
    }

    public abstract void attack(Attack attack);

}
