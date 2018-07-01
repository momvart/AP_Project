package models.attack.attackHelpers;

import exceptions.SoldierNotFoundException;
import models.attack.Attack;
import models.buildings.DefensiveTower;
import models.soldiers.MoveType;
import models.soldiers.Soldier;
import utils.Point;

import java.util.ArrayList;
import java.util.stream.Collectors;

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
        if (soldierPoint != null && attack.getSoldiersOnLocations().getSoldiers(soldierPoint, MoveType.GROUND).anyMatch(x -> !x.getAttackHelper().isDead))
        {
            if (mainTargets != null && mainTargets.size() != 0)
                triggerListener.onBulletTrigger(attack.getSoldiersOnLocations().getSoldiers(soldierPoint, MoveType.GROUND).collect(Collectors.toList()).get(0).getAttackHelper().getGraphicHelper().getDrawer().getPosition(), mainTargets.get(0));
        }
    }

    @Override
    public void attack()
    {
        if (mainTargets != null && mainTargets.size() > 0)
        {
            Soldier targetSoldier = mainTargets.get(0);
            targetSoldier.getAttackHelper().decreaseHealth(getTower().getDamagePower());
        }

        mainTargets = null;
        // TODO: 6/6/18 don't change mainTargets and wholeTargets to null  each turn for some towers
    }
}