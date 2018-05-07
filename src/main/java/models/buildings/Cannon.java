package models.buildings;

import exceptions.SoldierNotFoundException;
import models.Attack;
import models.soldiers.Soldier;
import utils.Point;

import java.util.List;
import java.util.stream.Stream;

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
            Point soldier = attack.getNearestSoldier(this.location, getRange(), getDefenseType().convertToMoveType());
            Stream<Soldier> soldiers = attack.getSoldiersOnLocations().getSoldiers(soldier, getDefenseType().convertToMoveType());
            soldiers.forEach(soldier1 -> soldier1.getAttackHelper().decreaseHealth(getDamagePower()));
            soldiersInRange = attack.getSoldiersInRange(this.location, SECOND_RANGE, getDefenseType().convertToMoveType());
            soldiersInRange.forEach(soldier1 -> soldier1.getAttackHelper().decreaseHealth(getDamagePower() - 1));
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
