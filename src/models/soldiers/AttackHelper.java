package models.soldiers;


import models.Attack;

public abstract class AttackHelper
{
    protected Attack attack;

    public AttackHelper(Attack attack)
    {
        this.attack = attack;
    }

    public abstract void move();

    public abstract void fire();

    public abstract void setTarget();
}
