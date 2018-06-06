package models.buildings;

import exceptions.SoldierNotFoundException;
import models.Attack;
import models.soldiers.Soldier;
import utils.Point;

import java.util.ArrayList;
import java.util.List;

public class DefensiveTowerAttackHelper extends BuildingAttackHelper
{

    protected static final int SECOND_RANGE = 2;
    protected ArrayList<Soldier> mainTargets;
    protected ArrayList<Soldier> wholeTargets;

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
        if (mainTargets != null && mainTargets.size() > 0)
            mainTargets.get(0).getAttackHelper().decreaseHealth(defensiveTower.getDamagePower());
        mainTargets = null;
        wholeTargets = null;
        // TODO: 6/6/18 don't change mainTargets and wholeTargets to null  each turn for some towers
    }

    public void setTarget()
    {
        DefensiveTower defensiveTower = (DefensiveTower)building;
        try { setTypicalTowersTarget(defensiveTower); }
        catch (SoldierNotFoundException ignored) { }
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
