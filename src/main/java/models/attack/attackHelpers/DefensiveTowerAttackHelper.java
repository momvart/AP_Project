package models.attack.attackHelpers;

import graphics.helpers.AttackBuildingGraphicHelper;
import graphics.helpers.DefensiveTowerGraphicHelper;
import graphics.helpers.IOnBulletTriggerListener;
import models.attack.Attack;
import models.buildings.DefensiveTower;
import models.soldiers.Soldier;

import java.util.ArrayList;
import java.util.List;

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
        //this method is expired
        /*
        super.passTurn();
        if (!destroyed)
        {
            try { setTarget(); }
            catch (SoldierNotFoundException ignored) {}
            attack();
        }
        */
    }

    public Soldier getAnAliveSoldier(List<Soldier> mainTargets)
    {
        if (mainTargets == null || mainTargets.size() == 0)
        {
            return null;
        }
        for (Soldier soldier : mainTargets)
        {
            if (!soldier.getAttackHelper().isDead())
            {
                return soldier;
            }
        }
        return null;
    }

    //graphcs
    protected IOnBulletTriggerListener triggerListener;

    public abstract void setTarget();

    public abstract void attack();

    @Override
    public void onReload()
    {
        if (!destroyed)
        {
            DefensiveTowerGraphicHelper towerGraphicHelper = (DefensiveTowerGraphicHelper)getGraphicHelper();
            if (towerGraphicHelper.getBullet().inProgress)
                return;
            setTarget();
        }
    }


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