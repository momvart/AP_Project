package models.soldiers;

import models.Attack;
import utils.Point;

public abstract class Soldier
{
    private int health;
    private int level;
    private Point location;
    private AttackHelper attackHelper;

    public abstract int getType();

    public AttackHelper getAttackHelper()
    {
        return attackHelper;
    }

    public int getLevel()
    {
        return level;
    }

    public int getHealth()
    {
        return health;
    }

    public void decreaseHealth(int amount)
    {
        health -= amount;
    }

    public int getDamage()
    {
        return SoldierValues.getSoldierInfo(this.getType()).getInitialDamage() + level;
    }

    public SoldierInfo getArmyUnitInfo()
    {
        return SoldierValues.getSoldierInfo(this.getType());
    }

    public void participateIn(Attack attack)
    {
        this.attackHelper = new GeneralAttackHelper(attack);
    }

    public Point getLocation()
    {
        return location;
    }

    public void setLocation(Point location)
    {
        this.location = location;
    }

    public void heal()
    {
        this.health = SoldierValues.getSoldierInfo(this.getType()).getInitialHealth();
    }
}
