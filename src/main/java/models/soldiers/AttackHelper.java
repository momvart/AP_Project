package models.soldiers;


import models.Attack;
import models.buildings.Building;
import utils.Point;

public abstract class AttackHelper
{
    protected Attack attack;
    private boolean isSoldierDeployed;
    private Building soldierFavouriteTarget;
    private Point soldierLocation;
    private int soldierDamagePotential;
    private int soldierRange;

    public boolean isSoldierDeployed()
    {
        return isSoldierDeployed;
    }

    public void setSoldierIsDeployed(boolean isSoldierDeployed)
    {
        this.isSoldierDeployed = isSoldierDeployed;
    }


    public AttackHelper(Attack attack, Building favouriteTarget, Point location, int soldierDamagePotential, int soldierRange)
    {
        this.attack = attack;
        this.soldierFavouriteTarget = favouriteTarget;
        this.soldierLocation = location;
        this.soldierDamagePotential = soldierDamagePotential;
        this.soldierRange = soldierRange;
    }

    public Building getSoldierFavouriteTarget()
    {
        return soldierFavouriteTarget;
    }

    public Point getSoldierLocation()
    {
        return soldierLocation;
    }

    public int getSoldierDamagePotential()
    {
        return soldierDamagePotential;
    }

    public int getSoldierRange()
    {
        return soldierRange;
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
