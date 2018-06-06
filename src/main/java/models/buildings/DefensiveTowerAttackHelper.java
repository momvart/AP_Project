package models.buildings;

import exceptions.SoldierNotFoundException;
import models.Attack;
import models.soldiers.Soldier;
import utils.Point;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DefensiveTowerAttackHelper extends BuildingAttackHelper
{

    private static final int SECOND_RANGE = 2;
    private ArrayList<Soldier> mainTargets;
    private ArrayList<Soldier> wholeTargets;

    public DefensiveTowerAttackHelper(Building building, Attack attack)
    {
        super(building, attack);
    }

    public ArrayList<Soldier> getMainTargets()
    {
        return mainTargets;
    }

    public void passTurn()
    {
        super.passTurn();
        if (!destroyed)
        {
            setTarget();
            attack();
        }
    }

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

    public void setTarget()
    {
        DefensiveTower defensiveTower = (DefensiveTower)building;
        if (defensiveTower.getType() == Cannon.DEFENSIVE_TOWER_TYPE || defensiveTower.getType() == WizardTower.DEFENSIVE_TOWER_TYPE)
            try { setCanonTypeTowersTarget(defensiveTower); }
            catch (SoldierNotFoundException ignored) { }
        else
            try { setTypicalTowersTarget(defensiveTower); }
            catch (SoldierNotFoundException ignored) { }

    }

    private void setCanonTypeTowersTarget(DefensiveTower defensiveTower) throws SoldierNotFoundException
    {
        mainTargets = new ArrayList<>();
        wholeTargets = new ArrayList<>();
        List<Soldier> soldiersInRange = null;
        Point soldier = attack.getNearestSoldier(defensiveTower.location, defensiveTower.getRange(), defensiveTower.getDefenseType().convertToMoveType());
        Stream<Soldier> soldiers = attack.getSoldiersOnLocations().getSoldiers(soldier, defensiveTower.getDefenseType().convertToMoveType());
        mainTargets.addAll(soldiers.collect(Collectors.toList()));
        soldiersInRange = attack.getSoldiersInRange(defensiveTower.location, SECOND_RANGE, defensiveTower.getDefenseType().convertToMoveType());
        // TODO: 6/6/18 Change Second range.
        if (soldiersInRange != null)
            wholeTargets.addAll(soldiersInRange);

    }

    private void setTypicalTowersTarget(DefensiveTower defensiveTower) throws SoldierNotFoundException
    {
        Point soldierPoint = null;
        mainTargets = new ArrayList<>();
        soldierPoint = attack.getNearestSoldier(defensiveTower.location, defensiveTower.getRange(), defensiveTower.getDefenseType().convertToMoveType());
        List<Soldier> soldiers = attack.getSoldiersOnLocations().getSoldiers(soldierPoint);
        mainTargets.addAll(soldiers);
    }
}
