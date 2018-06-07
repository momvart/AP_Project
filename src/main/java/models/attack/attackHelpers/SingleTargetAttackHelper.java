package models.attack.attackHelpers;

import exceptions.SoldierNotFoundException;
import models.attack.Attack;
import models.buildings.DefensiveTower;
import utils.Point;

import java.util.ArrayList;

public class SingleTargetAttackHelper extends DefensiveTowerAttackHelper
{

    public SingleTargetAttackHelper(DefensiveTower building, Attack attack)
    {
        super(building, attack);
    }


    @Override
    public void setTarget() throws SoldierNotFoundException
    {
        DefensiveTower tower = getTower();
        Point soldierPoint = attack.getNearestSoldier(tower.getLocation(), tower.getRange(), tower.getDefenseType().convertToMoveType());
        mainTargets = new ArrayList<>(attack.getSoldiersOnLocations().getSoldiers(soldierPoint));
    }

    @Override
    public void attack()
    {
        if (mainTargets != null && mainTargets.size() > 0)
            mainTargets.get(0).getAttackHelper().decreaseHealth(getTower().getDamagePower());
        mainTargets = null;
        // TODO: 6/6/18 don't change mainTargets and wholeTargets to null  each turn for some towers
    }
}
