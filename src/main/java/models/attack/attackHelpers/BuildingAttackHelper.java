package models.attack.attackHelpers;

import graphics.helpers.AttackBuildingGraphicHelper;
import graphics.helpers.IOnDestroyListener;
import graphics.helpers.IOnReloadListener;
import models.attack.Attack;
import models.buildings.Building;

public class BuildingAttackHelper implements IOnReloadListener
{
    protected Building building;
    private final int startStrength;
    protected int strength;
    protected boolean destroyed;
    protected Attack attack;

    public BuildingAttackHelper(Building building, Attack attack)
    {
        startStrength = building.getBuildingInfo().getInitialStrength() + building.getBuildingInfo().getUpgradeStrengthInc() * building.getLevel();
        strength = startStrength;
        this.building = building;
        this.attack = attack;
    }

    public Building getBuilding()
    {
        return building;
    }

    public int getStartStrength()
    {
        return startStrength;
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
