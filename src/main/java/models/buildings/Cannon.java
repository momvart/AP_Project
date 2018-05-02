package models.buildings;

import models.Attack;
import models.soldiers.Soldier;
import utils.Point;

import java.util.List;

public class Cannon extends DefensiveTower
{
    public static final int DEFENSIVE_TOWER_TYPE = 9;

    public Cannon(Point location, int buildingNum)
    {
        super(location, buildingNum);
    }

    @Override
    public void attack(Attack attack)
    {
        List<Soldier> soldiersInRange = attack.getSoldiersInRange(this.location, getRange());
        soldiersInRange.forEach(soldier -> soldier.decreaseHealth(this.getDamagePower()));
    }

    @Override
    public int getType()
    {
        return 9;
    }
}
