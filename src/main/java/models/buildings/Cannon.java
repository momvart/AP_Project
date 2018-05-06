package models.buildings;

import exceptions.SoldierNotFoundException;
import models.Attack;
import models.soldiers.Soldier;
import utils.Point;

import java.util.List;

public class Cannon extends DefensiveTower
{
    public static final int DEFENSIVE_TOWER_TYPE = 9;
    private static final int SECOND_RANGE = 2;
    public Cannon(Point location, int buildingNum)
    {
        super(location, buildingNum);
    }

    @Override
    public void attack(Attack attack)
    {
        List<Soldier> soldiersInRange = null;
        try
        {
            soldiersInRange = attack.getSoldiersInRange(this.location, getRange(), SECOND_RANGE);
            soldiersInRange.forEach(soldier -> soldier.decreaseHealth(this.getDamagePower()));
        }
        catch (SoldierNotFoundException e)
        {
            //do nothing
        }
    }

    @Override
    public int getType()
    {
        return 9;
    }
}
