package models.soldiers;

import models.Attack;
import utils.Point;

public abstract class Soldier
{
    private int health;
    private int level;
    private Point location;
    private AttackHelper attackHelper;

    public Soldier()
    {

    }

    public Soldier(int level)
    {
        this.level = level;
        health = getInitialHelthOfUnitThisLevel();
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
        if (health == 0)
            attackHelper.setDead(true);
    }

    public int getDamage()
    {
        return getSoldierInfo().getInitialDamage() + level;
    }

    public MoveType getMoveType()
    {
        return getSoldierInfo().getMoveType();
    }

    public SoldierInfo getSoldierInfo()
    {
        return SoldierValues.getSoldierInfo(this.getType());
    }

    public void participateIn(Attack attack)
    {
        this.attackHelper = new GeneralAttackHelper(attack, this);
    }

    public boolean isParticipating(Attack attack)
    {
        if (this.attackHelper == null)
            return false;
        return this.attackHelper.attack == attack;
    }

    public Point getLocation()
    {
        return location;
    }

    public void setLocation(Point location)
    {
        this.location.setX(location.getX());
        this.location.setY(location.getY());
    }

    public void setAttackHelper(AttackHelper attackHelper)
    {
        this.attackHelper = attackHelper;//TODO‌ note that this parts application is in healer participateIn method
    }

    public void heal()
    {
        this.health = getInitialHelthOfUnitThisLevel();
    }

    public int getInitialHelthOfUnitThisLevel()
    {
        return SoldierValues.getSoldierInfo(this.getType()).getInitialHealth() + (level) * 5;
    }

    public void increaseHealth(int amount)
    {
        this.health = Math.min(this.getHealth() + amount, getInitialHelthOfUnitThisLevel());
    }

    public int getSpeed()
    {
        return SoldierValues.getSoldierInfo(getType()).getSpeed();
    }
}
