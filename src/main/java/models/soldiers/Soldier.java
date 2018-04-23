package models.soldiers;

import models.Attack;
import models.World;
import models.buildings.Barracks;
import utils.Point;

public abstract class Soldier
{
    private int health;
    private int level;
    private Point location;
    private AttackHelper attackHelper;

    public Soldier(int level)
    {
        health = SoldierValues.getSoldierInfo(this.getType()).getInitialHealth();
        this.level = level;
        location = new Point(-1, -1);
    }

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
        health = Math.max(health - amount, 0);
    }

    public int getDamage()
    {
        return SoldierValues.getSoldierInfo(this.getType()).getInitialDamage() + level - 1;
    }

    public SoldierInfo getSoldierInfo()
    {
        return SoldierValues.getSoldierInfo(this.getType());
    }

    public void participateIn(Attack attack)
    {
        this.attackHelper = new GeneralAttackHelper(attack, this, this.getLocation(), this.getDamage(), SoldierValues.getSoldierInfo(this.getType()).getRange());
    }

    public Point getLocation()
    {
        return location;
    }

    public void setLocation(Point location)
    {
        this.location = location;
    }

    public void setAttackHelper(AttackHelper attackHelper)
    {
        this.attackHelper = attackHelper;//TODO‌ note that this parts application is in healer participateIn method
    }

    public void heal()
    {
        this.health = getInitialHelathOfUnitThisLevel();
    }

    private int getInitialHelathOfUnitThisLevel()
    {
        return SoldierValues.getSoldierInfo(this.getType()).getInitialDamage() + (level - 1) * 5;//TODO levels are supposed starting from 1
    }

    public void increaseHealth(int amount)
    {
        this.health = Math.min(this.getHealth() + amount, getInitialHelathOfUnitThisLevel());
    }
}
