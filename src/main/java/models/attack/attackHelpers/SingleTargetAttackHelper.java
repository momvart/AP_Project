package models.attack.attackHelpers;

import exceptions.SoldierNotFoundException;
import models.attack.Attack;
import models.buildings.DefensiveTower;
import models.soldiers.Soldier;
import utils.Point;

import java.util.ArrayList;

public class SingleTargetAttackHelper extends DefensiveTowerAttackHelper
{

    public SingleTargetAttackHelper(DefensiveTower building, Attack attack)
    {
        super(building, attack);
    }


    Soldier targetSoldier;
    @Override
    public void setTarget(boolean networkPermission)
    {
        if (!isReal && !networkPermission)
            return;
        DefensiveTower tower = getTower();
        Point soldierPoint = null;
        try
        {
            soldierPoint = attack.getNearestSoldier(tower.getLocation(), tower.getRange(), tower.getDefenseType().convertToMoveType());
        }
        catch (SoldierNotFoundException e) {}
        if (soldierPoint == null)
            return;
        mainTargets = new ArrayList<>(attack.getSoldiersOnLocations().getSoldiers(soldierPoint));
        targetSoldier = getAnAliveSoldier(mainTargets);
        if (targetSoldier == null)
            return;
        triggerListener.onBulletTrigger(targetSoldier.getAttackHelper().getGraphicHelper().getDrawer().getPosition(), targetSoldier);
        if (isReal)
            NetworkHelper.buildingSetTarget(this);
    }

    @Override
    public void attack(boolean networkPermission)
    {
        if (!isReal && !networkPermission)
            return;
        targetSoldier.getAttackHelper().decreaseHealth(getTower().getDamagePower());
        mainTargets = null;
        targetSoldier = null;
        if (isReal)
            NetworkHelper.buildingAttack(this);
        // TODO: 6/6/18 don't change mainTargets and wholeTargets to null  each turn for some towers
    }
}