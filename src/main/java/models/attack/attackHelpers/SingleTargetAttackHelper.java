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
    public void setTarget() throws SoldierNotFoundException
    {
        DefensiveTower tower = getTower();
        Point soldierPoint = attack.getNearestSoldier(tower.getLocation(), tower.getRange(), tower.getDefenseType().convertToMoveType());
        mainTargets = new ArrayList<>(attack.getSoldiersOnLocations().getSoldiers(soldierPoint));
        targetSoldier = getAnAliveSoldier(mainTargets);
        if (targetSoldier == null)
            return;
        triggerListener.onBulletTrigger(targetSoldier.getAttackHelper().getGraphicHelper().getDrawer().getPosition(), targetSoldier);
    }

    @Override
    public void attack()
    {
        targetSoldier.getAttackHelper().decreaseHealth(getTower().getDamagePower());
        mainTargets = null;
        targetSoldier = null;
        // TODO: 6/6/18 don't change mainTargets and wholeTargets to null  each turn for some towers
    }
}