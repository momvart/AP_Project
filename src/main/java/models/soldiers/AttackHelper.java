package models.soldiers;


import models.Attack;
import utils.Point;

import java.util.List;

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

    public void passTurn()
    {
        removeSoldierIfDead();
        if (soldier != null && !isDead && isSoldierDeployed)
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
            setDead(true);
            soldier = null;
        }
    }

    protected Point getPointToGo(Point destination)
    {
        List<Point> soldierPath = attack.getSoldierPath(getSoldierLocation(), destination, soldier.getMoveType() == MoveType.AIR);
        Point pointToGo = soldierPath.get(soldierPath.size() - 1);

        int i;
        for (i = soldierPath.size() - 1; i >= 0; i--)
        {
            if (i != soldierPath.size() - 1)
                pointToGo = soldierPath.get(i + 1);
            if (Point.euclideanDistance(soldierPath.get(i), getSoldierLocation()) > soldier.getSpeed())
                break;
        }
        return pointToGo;
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
