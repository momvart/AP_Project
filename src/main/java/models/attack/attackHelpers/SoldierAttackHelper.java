package models.attack.attackHelpers;


import graphics.helpers.IOnMoveFinishedListener;
import graphics.helpers.IOnReloadListener;
import graphics.helpers.SoldierGraphicHelper;
import models.attack.Attack;
import models.soldiers.MoveType;
import models.soldiers.Soldier;
import models.soldiers.SoldierValues;
import utils.Point;
import utils.PointF;

import java.util.ArrayList;
import java.util.List;

public abstract class SoldierAttackHelper implements IOnReloadListener, IOnMoveFinishedListener
{
    private int health;
    protected Attack attack;
    protected Soldier soldier;
    private boolean isSoldierDeployed = false;
    public boolean readyToFireTarget = false;
    protected boolean isReal;
    //protected boolean isDead = false;

    public SoldierAttackHelper(Attack attack, Soldier soldier)
    {
        this.attack = attack;
        this.soldier = soldier;
        this.health = getInitialHealth();
    }

    public void setIsReal()
    {
        isReal = attack.isReal;
    }

    public boolean isReal()
    {
        return isReal;
    }

    public int getHealth()
    {
        return health;
    }

    public void increaseHealth(int amount, boolean networkPermission)
    {
        setHealth(amount + health, networkPermission);
    }

    public void decreaseHealth(int amount, boolean networkPermission)
    {
        setHealth(health - amount, networkPermission);
    }

    public void setHealth(int health, boolean networkPermission)
    {
        if (!isReal && !networkPermission && !isDead())
            return;

        this.health = Math.max(0, Math.min(health, getInitialHealth()));

        if (health <= 0)
            setDead(true);

        if (getGraphicHelper() != null)
            getGraphicHelper().updateDrawer();


        if (isReal)
            NetworkHelper.soldierSetHealth(soldier.getId(), health);
    }

    public int getInitialHealth()
    {
        return SoldierValues.getSoldierInfo(soldier.getType()).getInitialHealth() + (soldier.getLevel()) * 5;
    }

    public boolean isSoldierDeployed()
    {
        return isSoldierDeployed;
    }

    public void setSoldierIsDeployed(boolean isSoldierDeployed)
    {
        this.isSoldierDeployed = isSoldierDeployed;
    }

    public Point getSoldierLocation()
    {
        return soldier.getLocation();
    }

    public int getDamage()
    {
        return soldier.getDamage();
    }

    public int getRange()
    {
        return SoldierValues.getSoldierInfo(soldier.getType()).getRange();
    }

    public Attack getAttack()
    {
        return attack;
    }

    public void passTurn()
    {
        //pass turn method is expired
        /*
        removeSoldierIfDead();
        if (soldier != null && !isDead() && isSoldierDeployed)
        {
            setTarget();
            move();
            fire();
        }
        */
    }

    protected void removeSoldierIfDead()
    {
        if (isDead())
        {
            attack.getSoldiersOnLocations().pull(soldier, soldier.getLocation());
        }
    }

    public Point getPointToGo(Point destination)
    {
        List<Point> soldierPath = attack.getSoldierPath(getSoldierLocation(), destination, soldier.getMoveType() == MoveType.AIR);
        Point pointToGo = soldierPath.get(soldierPath.size() - 1);

        int i;
        for (i = soldierPath.size() - 1; i >= 0; i--)
        {
            if (i != soldierPath.size() - 1)
                pointToGo = soldierPath.get(i + 1);
            if (Point.euclideanDistance(soldierPath.get(i), getSoldierLocation()) > soldier.getSpeed())
                break;
        }
        return pointToGo;
    }


    public abstract void move();

    public abstract void fire();

    public abstract void setTarget();

    public abstract Point getTargetLocation();


    public boolean isDead()
    {
        return getHealth() <= 0;
    }

    public void setDead(boolean dead)
    {
//        isDead = dead;
    }

    private IOnDecampListener decampListener;

    public void setDecampListener(IOnDecampListener decampListener)
    {
        this.decampListener = decampListener;
    }

    protected void callOnDecamp()
    {
        readyToFireTarget = false;
        if (decampListener != null)
            decampListener.onDecamp();
    }

    private ArrayList<IOnSoldierDieListener> soldierDieListeners = new ArrayList<>();

    public void addSoldierDieListener(IOnSoldierDieListener soldierDieListener)
    {
        soldierDieListeners.add(soldierDieListener);
    }

    public void callOnSoldierDie()
    {
        soldierDieListeners.forEach(IOnSoldierDieListener::onSoldierDie);
        if (soldier.getAttackHelper().isReal)
            NetworkHelper.soldierDie(soldier.getId());
    }

    public void onMoveFinished(PointF currentPos)
    {
        readyToFireTarget = true;
    }

    //graphic
    private SoldierGraphicHelper graphicHelper;

    public SoldierGraphicHelper getGraphicHelper()
    {
        return graphicHelper;
    }

    public void setGraphicHelper(SoldierGraphicHelper graphicHelper)
    {
        this.graphicHelper = graphicHelper;
    }


    @Override
    public void onReload()
    {
        removeSoldierIfDead();
    }
}
