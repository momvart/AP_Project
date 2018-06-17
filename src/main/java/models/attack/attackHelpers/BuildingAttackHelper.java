package models.attack.attackHelpers;

import graphics.helpers.BuildingGraphicHelper;
import graphics.helpers.IOnDestroyListener;
import models.attack.Attack;
import models.buildings.Building;

public class BuildingAttackHelper
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

    IOnDestroyListener destroyListener;
    BuildingGraphicHelper bgh;

    public IOnDestroyListener getDestroyListener()
    {
        return destroyListener;
    }

    public void setDestroyListener(IOnDestroyListener destroyListener)
    {
        this.destroyListener = destroyListener;
    }

    public BuildingGraphicHelper getBgh()
    {
        return bgh;
    }

    public void setBgh(BuildingGraphicHelper bgh)
    {
        this.bgh = bgh;
    }
}
