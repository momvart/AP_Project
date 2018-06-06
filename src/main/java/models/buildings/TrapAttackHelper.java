package models.buildings;

import models.Attack;
import models.soldiers.Soldier;

import java.util.ArrayList;
import java.util.List;

public class TrapAttackHelper extends DefensiveTowerAttackHelper
{
    public TrapAttackHelper(Building building, Attack attack)
    {
        super(building, attack);
    }

    @Override
    public void setTarget()
    {
        mainTargets = new ArrayList<>();
        List<Soldier> soldiers = attack.getSoldiersOnLocations().getSoldiers(building.location);
        mainTargets.addAll(soldiers);
    }
}
