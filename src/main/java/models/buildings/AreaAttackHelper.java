package models.buildings;

import exceptions.SoldierNotFoundException;
import models.Attack;
import models.soldiers.Soldier;
import utils.Point;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AreaAttackHelper extends DefensiveTowerAttackHelper
{
    public AreaAttackHelper(Building building, Attack attack)
    {
        super(building, attack);
    }

    @Override
    public void setTarget()
    {
        DefensiveTower defensiveTower = (DefensiveTower)building;
        mainTargets = new ArrayList<>();
        wholeTargets = new ArrayList<>();
        List<Soldier> soldiersInRange = null;
        try
        {
            Point soldier = attack.getNearestSoldier(defensiveTower.location, defensiveTower.getRange(), defensiveTower.getDefenseType().convertToMoveType());
            Stream<Soldier> soldiers = attack.getSoldiersOnLocations().getSoldiers(soldier, defensiveTower.getDefenseType().convertToMoveType());
            mainTargets.addAll(soldiers.collect(Collectors.toList()));
            soldiersInRange = attack.getSoldiersInRange(defensiveTower.location, SECOND_RANGE, defensiveTower.getDefenseType().convertToMoveType());
            // TODO: 6/6/18 Change Second range.
            if (soldiersInRange != null)
                wholeTargets.addAll(soldiersInRange);
        }
        catch (SoldierNotFoundException ignored) { }
    }

    @Override
    public void attack()
    {
        DefensiveTower defensiveTower = (DefensiveTower)building;
        if (mainTargets != null)
            mainTargets.forEach(soldier -> soldier.getAttackHelper().decreaseHealth(defensiveTower.getDamagePower()));
        if (wholeTargets != null)
            wholeTargets.forEach(soldier -> soldier.getAttackHelper().decreaseHealth(defensiveTower.getDamagePower() - 1));
        mainTargets = null;
        wholeTargets = null;
    }
}
