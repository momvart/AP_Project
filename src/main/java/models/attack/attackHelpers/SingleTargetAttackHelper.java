package models.attack.attackHelpers;

import exceptions.SoldierNotFoundException;
import graphics.helpers.DefenseKind;
import models.attack.Attack;
import models.buildings.DefensiveTower;
import models.soldiers.Soldier;
import models.soldiers.SoldierInjuryReport;
import utils.Point;
import utils.PointF;

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
        towerGraphicHelper.setBulletUltimatePosition(new PointF(soldierPoint));
        mainTargets = new ArrayList<>(attack.getSoldiersOnLocations().getSoldiers(soldierPoint));
    }

    @Override
    public void attack()
    {
        if (mainTargets != null && mainTargets.size() > 0)
        {
            Soldier targetSoldier = mainTargets.get(0);
            int initialHealth = targetSoldier.getAttackHelper().getHealth();

            targetSoldier.getAttackHelper().decreaseHealth(getTower().getDamagePower());

            int finalHealth = Math.max(0, targetSoldier.getAttackHelper().getHealth());
            ArrayList<SoldierInjuryReport> soldierInjuryReports = new ArrayList<>();
            soldierInjuryReports.add(new SoldierInjuryReport(targetSoldier, initialHealth, finalHealth));
            fireListener.onDefenseFire(targetSoldier.getLocation(), DefenseKind.SINGLE_TARGET, soldierInjuryReports, null);
        }

        mainTargets = null;
        // TODO: 6/6/18 don't change mainTargets and wholeTargets to null  each turn for some towers
    }

    @Override
    public void onReload()
    {
        if (!destroyed)
        {
            try { setTarget(); }
            catch (SoldierNotFoundException ignored) {}
        }
        else
        {
            destroyListener.onDestroy();
        }
    }


    @Override
    public void onBulletHit()
    {
        attack();
    }
}
