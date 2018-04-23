package models.soldiers;

import models.Attack;
import utils.Point;

import java.util.ArrayList;

public class HealerAttackHelper extends AttackHelper
{
    private ArrayList<Soldier> targets;

    public HealerAttackHelper(Attack attack, Healer healer, Point location, int soldierDamagePotential, int soldierRange)
    {
        super(attack, healer, location, soldierDamagePotential, soldierRange);
    }

    @Override
    public void move()
    {

    }

    @Override
    public void fire()
    {
        if (targets != null && targets.size() != 0)
        {
            for (Soldier target : targets)
            {
                target.increaseHealth(getDamage());
            }
        }
    }

    @Override
    public void setTarget()
    {
        targets = getSoldiersInRange();
    }

    private ArrayList<Soldier> getSoldiersInRange()
    {
        ArrayList<Soldier> soldiersInRange = new ArrayList<>();
        for (Soldier soldier : attack.getSoldiersOnMap())
        {
            if (euclidianDistance(soldier.getLocation(), getSoldierLocation()) < getRange())
            {
                soldiersInRange.add(soldier);
            }
        }
        return soldiersInRange;
    }
}
