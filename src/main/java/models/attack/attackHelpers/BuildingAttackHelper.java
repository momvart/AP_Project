package models.attack.attackHelpers;

import graphics.helpers.AttackBuildingGraphicHelper;
import graphics.helpers.IOnDestroyListener;
import graphics.helpers.IOnReloadListener;
import models.attack.Attack;
import models.buildings.Building;

public class BuildingAttackHelper implements IOnReloadListener
{
    protected Building building;
    private final int initStrength;
    protected int strength;
    protected boolean destroyed;
    protected Attack attack;

    public BuildingAttackHelper(Building building, Attack attack)
    {
        initStrength = building.getBuildingInfo().getInitialStrength() + building.getBuildingInfo().getUpgradeStrengthInc() * building.getLevel();
        strength = initStrength;
        this.building = building;
        this.attack = attack;
    }

    public Building getBuilding()
    {
        return building;
    }

    public int getInitialStrength()
    {
        return initStrength;
    }

    public int getStrength()
    {
        return strength;
    }

    public void decreaseStrength(int amount)
    {
        this.strength -= amount;
        if (strength <= 0)
        {
            destroyed = true;
            callOnDestroyed();
        }

        if (getGraphicHelper() != null)
            getGraphicHelper().updateDrawer();
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

    @Override
    public void onReload()
    {

    }

    private IOnDestroyListener destroyListener;

    protected void callOnDestroyed()
    {
        if (destroyListener != null)
            destroyListener.onDestroy();
    }

    public void setDestroyListener(IOnDestroyListener destroyListener)
    {
        this.destroyListener = destroyListener;
    }

    private AttackBuildingGraphicHelper graphicHelper;

    public AttackBuildingGraphicHelper getGraphicHelper()
    {
        return graphicHelper;
    }

    public void setGraphicHelper(AttackBuildingGraphicHelper graphicHelper)
    {
        this.graphicHelper = graphicHelper;
    }

    public int getMaximumHealthThisLevel()
    {
        //TODO
        return 0;
    }
}
