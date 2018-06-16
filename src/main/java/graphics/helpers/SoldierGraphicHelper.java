package graphics.helpers;

import graphics.Layer;
import graphics.drawers.SoldierDrawer;
import graphics.positioning.PositioningSystem;
import models.attack.attackHelpers.*;
import models.buildings.BuildingDestructionReport;
import models.soldiers.*;
import utils.Point;
import utils.PointF;

import java.net.URISyntaxException;
import java.util.ArrayList;

import static java.lang.Math.floor;

public class SoldierGraphicHelper extends GraphicHelper implements IOnDecampListener, IOnSoldierDieListener, IOnSoldierFireListener, IOnHealerHealListener
{
    protected Soldier soldier;

    private SoldierDrawer drawer;

    protected PointF moveDest;
    private boolean isSoldierHealer = false;

    private IOnMoveFinishedListener moveListener;


    private int turn = 1;

    public SoldierGraphicHelper(Soldier soldier, Layer layer)
    {
        this.soldier = soldier;
        try
        {
            drawer = new SoldierDrawer(soldier);
        }
        catch (URISyntaxException e)
        {
            e.printStackTrace();
        }

        setReloadDuration(.7);//TODO‌ yet to be decided!! consider that we've got attack animation playing in the reload method. so we should decide this duration equal to attack animation playDuration in order of smoothness of graphic

        drawer.setPosition(soldier.getLocation().getX(), soldier.getLocation().getY());
        drawer.setLayer(layer);
    }

    public SoldierDrawer getDrawer()
    {
        return drawer;
    }

    public void setUpListeners()
    {
        setMoveListener(soldier.getAttackHelper());
        setReloadListener(soldier.getAttackHelper());
        if (isSoldierHealer)
        {
            HealerAttackHelper hah = (HealerAttackHelper)soldier.getAttackHelper();
            hah.setDecampListener(this);
            hah.setSoldierDieListener(this);
            hah.setOnHealerHealListener(this);
        }
        else
        {
            GeneralSoldierAttackHelper gsah = (GeneralSoldierAttackHelper)soldier.getAttackHelper();
            gsah.setOnSoldierDieListener(this);
            gsah.setDecampListener(this);
            gsah.setSoldierFireListener(this);
        }
    }

    private Status status;

    public Status getStatus()
    {
        return status;
    }

    public void makeIdle()
    {
        status = Status.IDLE;
        drawer.playAnimation(SoldierDrawer.IDLE);
    }

    private void makeAttack()
    {
        status = Status.ATTACK;//TODO ‌to be manipulated to specify the kind of attack of each soldier.
        drawer.playAnimation(SoldierDrawer.ATTACK);
    }

    private void makeDie()
    {
        status = Status.DIE;
        drawer.playAnimation(SoldierDrawer.DIE);
    }

    private void makeRun()
    {
        status = Status.RUN;
        drawer.playAnimation(SoldierDrawer.RUN);
    }

    public void startJoggingToward(PointF dest)
    {
        makeRun();
        moveDest = dest;
        PositioningSystem ps = getDrawer().getLayer().getPosSys();
        drawer.getCurrentAnim().setScale(ps.convertX(dest) < ps.convertX(drawer.getPosition()) ? -1 : 1, 1);
    }

    private void doReplacing(double deltaT)
    {
        if (status != Status.RUN)
            return;

        Point veryCurrentPoint = getVeryPoint(drawer.getPosition());

        Point nextPoint = soldier.getAttackHelper().getNextPathPoint(veryCurrentPoint, getVeryPoint(moveDest));
        if (nextPoint == null)
        {
            onMoveFinished();
            return;
        }

        double distance = PointF.euclideanDistance(moveDest, drawer.getPosition());
        double cos = (nextPoint.getX() - getVeryPoint(drawer.getPosition()).getX()) / distance;
        double sin = (nextPoint.getY() - getVeryPoint(drawer.getPosition()).getY()) / distance;
        double stepDistance = Point.euclideanDistance(veryCurrentPoint, nextPoint);

        if (distance < 0.01 || distance < stepDistance)
        {
            onMoveFinished();
            return;
        }
        PointF newPosition = new PointF(drawer.getPosition().getX() + cos * soldier.getSpeed() * deltaT, drawer.getPosition().getY() + sin * soldier.getSpeed() * deltaT);
        if (floor(newPosition.getX()) != floor(soldier.getLocation().getX()) || floor(newPosition.getY()) != floor(soldier.getLocation().getY()))
        {
            soldier.setLocation(new Point((int)floor(drawer.getPosition().getX()), (int)floor(drawer.getPosition().getY())));
        }
        drawer.setPosition(newPosition.getX(), newPosition.getY());
    }

    private Point getVeryPoint(PointF position)
    {
        return new Point((int)floor(position.getX()), (int)floor(position.getY()));
    }



    private void onMoveFinished()
    {
        makeIdle();
        drawer.setPosition(moveDest.getX(), moveDest.getY());
        soldier.setLocation(getVeryPoint(moveDest));
        if (moveListener != null)
            moveListener.onMoveFinished(drawer.getPosition());
    }

    public void setMoveListener(IOnMoveFinishedListener moveListener)
    {
        this.moveListener = moveListener;
    }

    @Override
    public void update(double deltaT)
    {
        super.update(deltaT);
        doReplacing(deltaT);
    }

    @Override
    protected void callOnReload()
    {
        GeneralSoldierAttackHelper gsah = (GeneralSoldierAttackHelper)soldier.getAttackHelper();
        if (turn == 1)
        {
            triggerSoldier();
            turn++;
        }
        if (status == Status.IDLE || status == Status.ATTACK)
        {
            makeAttack();
            System.out.println("strength is :" + gsah.getTarget().getAttackHelper().getStrength());
            super.callOnReload();
        }
    }

    private void triggerSoldier()
    {
        if (isSoldierHealer)
        {
            HealerAttackHelper hah = (HealerAttackHelper)soldier.getAttackHelper();
            hah.setTarget();
            if (hah.getDestination() != null)
            {
                startJoggingToward(new PointF(hah.getDestination()));
            }
        }
        else
        {
            GeneralSoldierAttackHelper gsah = (GeneralSoldierAttackHelper)soldier.getAttackHelper();
            gsah.setTarget();
            if (gsah.getTarget() != null)
            {
                startJoggingToward(new PointF(gsah.getTarget().getLocation()));
            }
        }
    }

    @Override
    public void onDecamp()
    {
        if (soldier.getAttackHelper().isDead() || soldier == null || soldier.getAttackHelper().getHealth() <= 0)
        {
            makeDie();
        }
        else
        {
            if (!isSoldierHealer)
            {
                GeneralSoldierAttackHelper gsah = (GeneralSoldierAttackHelper)soldier.getAttackHelper();
                Point newDest = gsah.getTarget().getLocation();
                if (newDest != null)
                {
                    startJoggingToward(new PointF(newDest));
                }
                else
                {
                    makeIdle();
                }
            }
            else
            {
                HealerAttackHelper hah = (HealerAttackHelper)soldier.getAttackHelper();
                Point newDest = hah.getDestination();
                if (newDest != null)
                {
                    startJoggingToward(new PointF(newDest));
                }
                else
                {
                    makeIdle();
                }
            }
        }
    }

    @Override
    public void onSoldierFire(Point locationOfTarget, BuildingDestructionReport bdr)
    {
        if (soldier.getType() == WallBreaker.SOLDIER_TYPE)
        {
            onWallBreakerFire();
        }
        else if (soldier.getType() == Archer.SOLDIER_TYPE)
        {
            onArcherFire();
        }
        else if (soldier.getType() == Dragon.SOLDIER_TYPE)
        {
            onDragonFire();
        }
        else
        {
            onManToManFighterFire();
        }
    }

    private void onManToManFighterFire()
    {

    }

    private void onDragonFire()
    {

    }

    private void onArcherFire()
    {

    }

    private void onWallBreakerFire()
    {

    }

    @Override
    public void onHeal(ArrayList<SoldiersHealReport> reports)
    {
        //...
    }


    public enum Status
    {
        IDLE,
        DIE,
        RUN,
        ATTACK;

    }

    @Override
    public void onSoldierDie()
    {
        makeDie();
    }
}
