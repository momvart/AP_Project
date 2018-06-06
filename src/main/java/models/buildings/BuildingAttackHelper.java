package models.buildings;

import models.Attack;

public class BuildingAttackHelper
{
    protected Building building;
    protected transient int strength;
    protected boolean destroyed;
    protected Attack attack;

    public BuildingAttackHelper(Building building, Attack attack)
    {
        strength = building.getBuildingInfo().getInitialStrength() + building.getBuildingInfo().getUpgradeStrengthInc() * building.level;
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

}
