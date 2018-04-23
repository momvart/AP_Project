package models.soldiers;


import models.Attack;
import models.buildings.Building;
import utils.Point;

public abstract class AttackHelper
{
    protected Attack attack;
    protected Soldier soldier;
    private boolean soldierDeployed;
    private Point soldierLocation;
    private int damage;
    private int range;

    public boolean isSoldierDeployed()
    {
        return soldierDeployed;
    }

    public void setSoldierIsDeployed(boolean isSoldierDeployed)
    {
        this.soldierDeployed = isSoldierDeployed;
    }


    public AttackHelper(Attack attack, Soldier soldier, Point location, int damage, int range)
    {
        this.attack = attack;
        this.soldier = soldier;
        this.soldierLocation = location;
        this.damage = damage;
        this.range = range;
    }

    public Point getSoldierLocation()
    {
        return soldierLocation;
    }

    public int getDamage()
    {
        return damage;
    }

    public int getRange()
    {
        return range;
    }

    public int euclidianDistance(Point location1, Point location2)
    {
        return (int)Math.floor(Math.sqrt(Math.pow(location1.getX() - location2.getX(), 2) + Math.pow(location1.getY() - location2.getY(), 2)));
    }

    public Integer manhatanianDistance(Point location1, Point location2)
    {
        return Math.abs(location1.getX() - location2.getX()) + Math.abs(location1.getY() - location2.getY());
    }

    public abstract void move();

    public abstract void fire();

    public abstract void setTarget();
}
