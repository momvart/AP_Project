package models.soldiers;

import models.attack.Attack;
import models.attack.attackHelpers.SoldierAttackHelper;
import models.attack.attackHelpers.GeneralSoldierAttackHelper;
import utils.Point;

import java.net.URISyntaxException;

public abstract class Soldier
{
    private int level;
    private Point location;
    protected SoldierAttackHelper attackHelper;

    public Soldier()
    {

    }

    public Soldier(int level)
    {
        this.level = level;
        location = new Point(-1, -1);
    }

    public abstract int getType();

    public SoldierAttackHelper getAttackHelper()
    {
        return attackHelper;
    }

    public int getLevel()
    {
        return level;
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

    public void participateIn(Attack attack) throws URISyntaxException
    {
        this.attackHelper = new GeneralSoldierAttackHelper(attack, this);
    }

    public boolean isParticipating(Attack attack)
    {
        if (this.attackHelper == null)
            return false;
        return this.attackHelper.getAttack() == attack;
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


    public int getSpeed()
    {
        return SoldierValues.getSoldierInfo(getType()).getSpeed();
    }
}
