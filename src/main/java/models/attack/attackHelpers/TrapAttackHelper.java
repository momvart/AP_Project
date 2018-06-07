package models.attack.attackHelpers;

import models.attack.Attack;
import models.buildings.Building;
import models.buildings.DefensiveTower;
import models.buildings.Trap;
import models.soldiers.Soldier;

import java.util.ArrayList;
import java.util.List;

public class TrapAttackHelper extends AreaAttackHelper
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
    }

    @Override
    public void attack()
    {
        if (mainTargets != null && mainTargets.size() != 0)
        {
            super.attack();
            destroyed = true;
        }
    }
}
