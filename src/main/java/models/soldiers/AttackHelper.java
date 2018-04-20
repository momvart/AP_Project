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

    public boolean isSoldierDeployed()
    {
        return isSoldierDeployed;
    }

    public void setSoldierIsDeployed(boolean isSoldierDeployed)
    {
        this.isSoldierDeployed = isSoldierDeployed;
    }


    public AttackHelper(Attack attack, Building favouriteTarget, Point location)
    {
        this.attack = attack;
        this.soldierFavouriteTarget = favouriteTarget;
        this.soldierLocation = location;
    }

    public Building getSoldierFavouriteTarget()
    {
        return soldierFavouriteTarget;
    }

    public Point getSoldierLocation()
    {
        return soldierLocation;
    }

    public abstract void move();

    public abstract void fire();

    public abstract void setTarget();
}
