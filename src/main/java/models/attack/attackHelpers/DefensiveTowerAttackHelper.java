package models.attack.attackHelpers;

import exceptions.SoldierNotFoundException;
import graphics.helpers.IOnDefenseFireListener;
import graphics.helpers.IOnDestroyListener;
import models.attack.Attack;
import models.buildings.DefensiveTower;
import models.soldiers.Soldier;

import java.util.ArrayList;

public abstract class DefensiveTowerAttackHelper extends BuildingAttackHelper
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

    public abstract void setTarget() throws SoldierNotFoundException;

    public abstract void attack();

    //graphcs
    IOnDefenseFireListener fireListener;
    IOnDestroyListener destroyListener;

    public IOnDefenseFireListener getFireListener()
    {
        return fireListener;
    }

    public void setFireListener(IOnDefenseFireListener fireListener)
    {
        this.fireListener = fireListener;
    }

    public IOnDestroyListener getDestroyListener()
    {
        return destroyListener;
    }

    public void setDestroyListener(IOnDestroyListener destroyListener)
    {
        this.destroyListener = destroyListener;
    }
}
