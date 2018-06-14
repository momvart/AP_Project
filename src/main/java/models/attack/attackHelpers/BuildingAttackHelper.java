package models.attack.attackHelpers;

import graphics.helpers.DefensiveTowerGraphicHelper;
import graphics.helpers.IOnReloadListener;
import models.attack.Attack;
import models.buildings.Building;

public class BuildingAttackHelper implements IOnReloadListener
{
    protected Building building;
    protected int strength;
    protected boolean destroyed;
    protected Attack attack;

    public BuildingAttackHelper(Building building, Attack attack)
    {
        strength = building.getBuildingInfo().getInitialStrength() + building.getBuildingInfo().getUpgradeStrengthInc() * building.getLevel();
        this.building = building;
        this.attack = attack;
    }

    public Building getBuilding()
    {
        return building;
    }

    public int getStrength()
    {
        return strength;
    }

    public void decreaseStrength(int amount)
    {
        this.strength -= amount;
    }

    public boolean isDestroyed()
    {
        return destroyed;
    }

    public Attack getAttack()
    {
        return attack;
    }

    public void passTurn()
    {
        if (strength <= 0)
            destroyed = true;
    }

    //graphics
    DefensiveTowerGraphicHelper bgh;

    public void setBgh(DefensiveTowerGraphicHelper bgh)
    {
        this.bgh = bgh;
    }

    @Override
    public void onReload()
    {

    }
}
