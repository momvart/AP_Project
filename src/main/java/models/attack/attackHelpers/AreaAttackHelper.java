package models.attack.attackHelpers;

import exceptions.SoldierNotFoundException;
import models.attack.Attack;
import models.buildings.DefensiveTower;
import models.buildings.WizardTower;
import models.soldiers.Soldier;
import utils.Point;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AreaAttackHelper extends DefensiveTowerAttackHelper
{
    private ArrayList<Soldier> wholeTargets;

    public AreaAttackHelper(DefensiveTower building, Attack attack)
    {
        super(building, attack);
    }

    Point soldier;
    Soldier targetSoldier;
    @Override
    public void setTarget()
    {
        if (!isReal)
            return;
        DefensiveTower defensiveTower = (DefensiveTower)building;
        try
        {
            soldier = attack.getNearestSoldier(defensiveTower.getLocation(), defensiveTower.getRange(), defensiveTower.getDefenseType().convertToMoveType());
        }
        catch (Exception e)
        {
            return;
        }
        targetSoldier = getAnAliveSoldier(attack.getSoldiersOnLocations().getSoldiers(soldier));
        if (targetSoldier == null)
            return;


        if (building.getType() == WizardTower.DEFENSIVE_TOWER_TYPE)
            triggerListener.onBulletTrigger(targetSoldier.getAttackHelper().getGraphicHelper().getDrawer().getPosition(), targetSoldier);
        else
            triggerListener.onBulletTrigger(soldier.toPointF(), null);
    }

    @Override
    public void attack()
    {
        if (!isReal)
            return;
        soldier = targetSoldier.getLocation();
        DefensiveTower defensiveTower = (DefensiveTower)building;
        List<Soldier> soldiersInRange = null;
        Stream<Soldier> soldiers = attack.getSoldiersOnLocations().getSoldiers(soldier, defensiveTower.getDefenseType().convertToMoveType());
        mainTargets = new ArrayList<>();
        wholeTargets = new ArrayList<>();
        mainTargets.addAll(soldiers.collect(Collectors.toList()));
        try
        {
            soldiersInRange = attack.getSoldiersInRange(defensiveTower.getLocation(), SECOND_RANGE, defensiveTower.getDefenseType().convertToMoveType());
        }
        catch (SoldierNotFoundException e) {}
        // TODO: 6/6/18 Change Second range.
        if (soldiersInRange != null)
            wholeTargets.addAll(soldiersInRange);
        DefensiveTower tower = (DefensiveTower)building;
        if (mainTargets != null)
            for (Soldier soldier : mainTargets)
                soldier.getAttackHelper().decreaseHealth(tower.getDamagePower(), false);
        if (wholeTargets != null)
            for (Soldier soldier : wholeTargets)
                soldier.getAttackHelper().decreaseHealth(tower.getDamagePower() - 1, false);
        mainTargets = null;
        wholeTargets = null;
    }

}