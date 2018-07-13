package models.attack.attackHelpers;

import graphics.helpers.AttackBuildingGraphicHelper;
import graphics.helpers.IOnDestroyListener;
import graphics.helpers.IOnReloadListener;
import models.attack.Attack;
import models.buildings.Building;

import java.util.ArrayList;

public class BuildingAttackHelper implements IOnReloadListener
{
    protected Building building;
    private final int initStrength;
    protected int strength;
    protected boolean destroyed;
    protected Attack attack;
    protected boolean isReal;

    public BuildingAttackHelper(Building building, Attack attack)
    {
        initStrength = building.getBuildingInfo().getInitialStrength() + building.getBuildingInfo().getUpgradeStrengthInc() * building.getLevel();
        strength = initStrength;
        this.building = building;
        this.attack = attack;
    }

    public boolean isReal()
    {
        return isReal;
    }

    public void setBuildingIsReal()
    {
        isReal = attack.isReal;
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

    public void decreaseStrength(int amount, boolean networkPermission)
    {
        setStrength(strength - amount, networkPermission);
    }

    public void setStrength(int strength, boolean networkPermission)
    {
        if ((!isReal && !networkPermission) || isDestroyed())
            return;
        this.strength = strength;

        if (strength <= 0)
        {
            destroyed = true;
            callOnDestroyed();
        }

        if (getGraphicHelper() != null)
            getGraphicHelper().updateDrawer();

        if (isReal)
            NetworkHelper.buildingSetStrength(building.getId(), strength);
    }

    public boolean isDestroyed()
    {
        return strength <= 0;
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
    public void onReload() {}

    ArrayList<IOnDestroyListener> destroyListeners = new ArrayList<>();

    protected void callOnDestroyed()
    {
        destroyListeners.forEach(IOnDestroyListener::onDestroy);
        if (building.getAttackHelper().isReal)
            NetworkHelper.buildingDestroy(building.getId());
    }

    public void addDestroyListener(IOnDestroyListener destroyListener)
    {
        this.destroyListeners.add(destroyListener);
    }

    private AttackBuildingGraphicHelper graphicHelper;

    public AttackBuildingGraphicHelper getGraphicHelper()
    {
        return graphicHelper;
    }

    public void setGraphicHelper(AttackBuildingGraphicHelper graphicHelper)
    {
        this.graphicHelper = graphicHelper;
        addDestroyListener(graphicHelper);
    }

    public int getMaximumHealthThisLevel()
    {
        //TODO
        return 0;
    }
}
