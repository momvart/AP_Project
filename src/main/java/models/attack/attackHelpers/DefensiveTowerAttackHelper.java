package models.attack.attackHelpers;

import exceptions.SoldierNotFoundException;
import graphics.helpers.AttackBuildingGraphicHelper;
import graphics.helpers.DefensiveTowerGraphicHelper;
import graphics.helpers.IOnDefenseFireListener;
import graphics.helpers.IOnReloadListener;
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

    @Override
    public void onReload()
    {
        super.onReload();
        if (!destroyed)
        {
            try { setTarget(); }
            catch (SoldierNotFoundException ignored) {}
        }
    }

    public abstract void setTarget() throws SoldierNotFoundException;

    public abstract void attack();

    //graphcs
    IOnDefenseFireListener fireListener;

    public IOnDefenseFireListener getFireListener()
    {
        return fireListener;
    }

    DefensiveTowerGraphicHelper towerGraphicHelper;

    public void setDefenseFireListener(IOnDefenseFireListener defenseFireListener)
    {
        this.fireListener = defenseFireListener;
    }

    @Override
    public void setGraphicHelper(AttackBuildingGraphicHelper graphicHelper)
    {
        if (!(graphicHelper instanceof DefensiveTowerGraphicHelper))
            throw new IllegalArgumentException("Graphic helper should be a DefensiveTowerGraphicHelper.");
        super.setGraphicHelper(graphicHelper);

        DefensiveTowerGraphicHelper gh = (DefensiveTowerGraphicHelper)graphicHelper;
        gh.setBulletHitListener(this);
        this.setDefenseFireListener(gh);
    }

    @Override
    public void onBulletHit()
    {
        attack();
    }
}
