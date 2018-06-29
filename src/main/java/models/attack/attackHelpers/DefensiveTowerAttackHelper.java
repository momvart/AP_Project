package models.attack.attackHelpers;

import exceptions.SoldierNotFoundException;
import graphics.helpers.AttackBuildingGraphicHelper;
import graphics.helpers.DefensiveTowerGraphicHelper;
import graphics.helpers.IOnBulletTriggerListener;
import models.attack.Attack;
import models.buildings.DefensiveTower;
import models.soldiers.Soldier;

import java.util.ArrayList;

public abstract class DefensiveTowerAttackHelper extends BuildingAttackHelper implements IOnBulletHitListener
{

    protected static final int SECOND_RANGE = 2;
    protected ArrayList<Soldier> mainTargets;

    public DefensiveTowerAttackHelper(DefensiveTower building, Attack attack)
    {
        super(building, attack);
    }

    protected DefensiveTower getTower()
    {
        return (DefensiveTower)building;
    }

    public void passTurn()
    {
        super.passTurn();
        if (!destroyed)
        {
            try { setTarget(); }
            catch (SoldierNotFoundException ignored) {}
            attack();
        }
    }

    //graphcs
    protected IOnBulletTriggerListener triggerListener;

    public abstract void setTarget() throws SoldierNotFoundException;

    public abstract void attack();

    @Override
    public void onReload()
    {
        super.onReload();
        if (!destroyed)
        {
            DefensiveTowerGraphicHelper towerGraphicHelper = (DefensiveTowerGraphicHelper)getGraphicHelper();
            if (towerGraphicHelper.getBullet().inProgress)
                return;
            try
            {
                setTarget();
            }
            catch (SoldierNotFoundException ignored) {}
        }
    }

    DefensiveTowerGraphicHelper towerGraphicHelper;

    public void setTriggerListener(IOnBulletTriggerListener triggerListener)
    {
        this.triggerListener = triggerListener;
    }

    @Override
    public void setGraphicHelper(AttackBuildingGraphicHelper graphicHelper)
    {
        if (!(graphicHelper instanceof DefensiveTowerGraphicHelper))
            throw new IllegalArgumentException("Graphic helper should be a DefensiveTowerGraphicHelper.");
        super.setGraphicHelper(graphicHelper);

        DefensiveTowerGraphicHelper gh = (DefensiveTowerGraphicHelper)graphicHelper;
        gh.setBulletHitListener(this);
    }

    @Override
    public void onBulletHit()
    {
        attack();
    }
}
