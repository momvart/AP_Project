package models.soldiers;


import models.Attack;
import utils.Point;

public abstract class AttackHelper
{
    protected Attack attack;
    protected Soldier soldier;
    private boolean isSoldierDeployed = false;
    protected boolean isDead = false;

    public AttackHelper(Attack attack, Soldier soldier)
    {
        this.attack = attack;
        this.soldier = soldier;
    }

    public boolean isSoldierDeployed()
    {
        return isSoldierDeployed;
    }

    public void setSoldierIsDeployed(boolean isSoldierDeployed)
    {
        this.isSoldierDeployed = isSoldierDeployed;
    }

    public Point getSoldierLocation()
    {
        return soldier.getLocation();
    }

    public int getDamage()
    {
        return soldier.getDamage();
    }

    public int getRange()
    {
        return SoldierValues.getSoldierInfo(soldier.getType()).getRange();
    }

    public Integer manhatanianDistance(Point location1, Point location2)
    {
        return Math.abs(location1.getX() - location2.getX()) + Math.abs(location1.getY() - location2.getY());
    }

    public void passTurn()
    {
        removeSoldierIfDead();
        if (soldier != null && isSoldierDeployed)
        {
            setTarget();
            move();
            fire();
        }
    }

    public void removeSoldierIfDead()
    {
        if (soldier.getHealth() <= 0)
        {
            soldier = null;
        }
    }

    public abstract void move();

    public abstract void fire();

    public abstract void setTarget();

    public boolean isDead()
    {
        return isDead;
    }

    public void setDead(boolean dead)
    {
        isDead = dead;
    }
}
