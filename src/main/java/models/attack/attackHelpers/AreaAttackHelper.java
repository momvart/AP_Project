package models.attack.attackHelpers;

import exceptions.SoldierNotFoundException;
import graphics.helpers.DefenseKind;
import models.attack.Attack;
import models.buildings.DefensiveTower;
import models.soldiers.Soldier;
import models.soldiers.SoldierInjuryReport;
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
    @Override
    public void setTarget() throws SoldierNotFoundException
    {
        DefensiveTower defensiveTower = (DefensiveTower)building;
        mainTargets = new ArrayList<>();
        wholeTargets = new ArrayList<>();
        List<Soldier> soldiersInRange = null;
        soldier = attack.getNearestSoldier(defensiveTower.getLocation(), defensiveTower.getRange(), defensiveTower.getDefenseType().convertToMoveType());
        Stream<Soldier> soldiers = attack.getSoldiersOnLocations().getSoldiers(soldier, defensiveTower.getDefenseType().convertToMoveType());
        mainTargets.addAll(soldiers.collect(Collectors.toList()));
        soldiersInRange = attack.getSoldiersInRange(defensiveTower.getLocation(), SECOND_RANGE, defensiveTower.getDefenseType().convertToMoveType());
        // TODO: 6/6/18 Change Second range.
        if (soldiersInRange != null)
            wholeTargets.addAll(soldiersInRange);
    }

    @Override
    public void attack()
    {
        DefensiveTower tower = (DefensiveTower)building;
        ArrayList<SoldierInjuryReport> soldiersInjuredDirectly = new ArrayList<>();
        ArrayList<SoldierInjuryReport> soldiersInjuredImplicitly = new ArrayList<>();
        if (mainTargets != null)
            for (Soldier soldier : mainTargets)
            {
                int initialHealth = soldier.getAttackHelper().getHealth();
                soldier.getAttackHelper().decreaseHealth(tower.getDamagePower());
                int finalHealth = Math.max(soldier.getAttackHelper().getHealth(), 0);
                soldiersInjuredDirectly.add(new SoldierInjuryReport(soldier, initialHealth, finalHealth));
            }
        if (wholeTargets != null)
            for (Soldier soldier : wholeTargets)
            {
                int initialHealth = soldier.getAttackHelper().getHealth();
                soldier.getAttackHelper().decreaseHealth(tower.getDamagePower() - 1);
                int finalHealth = Math.max(soldier.getAttackHelper().getHealth(), 0);
                soldiersInjuredDirectly.add(new SoldierInjuryReport(soldier, initialHealth, finalHealth));
            }
        if ((mainTargets != null && mainTargets.size() != 0) || (wholeTargets != null && wholeTargets.size() != 0))
        {
            fireListener.onFire(soldier, DefenseKind.AREA_SPLASH, soldiersInjuredDirectly, soldiersInjuredImplicitly);
        }

        mainTargets = null;
        wholeTargets = null;
    }

    //graphcs

    @Override
    public void onReload()
    {
        if (!destroyed)
        {
            try { setTarget(); }
            catch (SoldierNotFoundException ignored) {}
            attack();
        }
        else
        {
            destroyListener.onDestroy();
        }
    }
}
