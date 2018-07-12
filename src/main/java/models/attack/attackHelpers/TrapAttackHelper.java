package models.attack.attackHelpers;

import models.attack.Attack;
import models.buildings.Trap;
import models.soldiers.Soldier;

import java.util.ArrayList;
import java.util.List;

public class TrapAttackHelper extends SingleTargetAttackHelper
{
    public TrapAttackHelper(Trap building, Attack attack)
    {
        super(building, attack);
    }

    @Override
    public void setTarget()
    {
        mainTargets = new ArrayList<>();
        List<Soldier> soldiers = attack.getSoldiersOnLocations().getSoldiers(building.getLocation());
        mainTargets.addAll(soldiers);
        if (mainTargets.size() > 0)
            targetSoldier = mainTargets.get(0);
    }

    @Override
    public void attack()
    {
        if (mainTargets != null && mainTargets.size() != 0)
        {
            super.attack();
            decreaseStrength(getStrength());
        }
    }

    @Override
    public void onReload()
    {
        super.onReload();
        attack();
    }
}
