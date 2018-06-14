package graphics.helpers;

import graphics.drawers.SoldierDrawer;
import graphics.positioning.PositioningSystem;
import models.attack.attackHelpers.GeneralSoldierAttackHelper;
import models.attack.attackHelpers.HealerAttackHelper;
import models.attack.attackHelpers.IOnDecampListener;
import models.attack.attackHelpers.IOnSoldierDieListener;
import models.soldiers.Healer;
import models.soldiers.Soldier;
import utils.Point;
import utils.PointF;

import java.net.URISyntaxException;

import static java.lang.Math.round;

public class SoldierGraphicHelper extends GraphicHelper implements IOnDecampListener, IOnSoldierDieListener
{
    protected Soldier soldier;

    private SoldierDrawer drawer;

    protected PointF moveDest;
    private boolean isSoldierHealer = false;

    private IOnMoveFinishedListener moveListener;


    public SoldierGraphicHelper(Soldier soldier)
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

        //initiating the initial states
        drawer.setPosition(soldier.getLocation().getX(), soldier.getLocation().getY());
        status = Status.RUN;
        settingUpListeners();
        triggeringTheSoldier();
    }

    private void triggeringTheSoldier()
    {
        if (soldier.getType() == Healer.SOLDIER_TYPE)
        {
            isSoldierHealer = true;
            HealerAttackHelper hah = (HealerAttackHelper)soldier.getAttackHelper();
            hah.setTarget();
            moveTo(new PointF(hah.getDestination()));
        }
        else
        {
            GeneralSoldierAttackHelper gsah = (GeneralSoldierAttackHelper)soldier.getAttackHelper();
            gsah.setTarget();
            moveTo(new PointF(gsah.getTarget().getLocation()));
        }
    }

    private void settingUpListeners()
    {
        setMoveListener(soldier.getAttackHelper());
        setReloadListener(soldier.getAttackHelper());
        if (isSoldierHealer)
        {
            HealerAttackHelper hah = (HealerAttackHelper)soldier.getAttackHelper();
            hah.setDecampListener(this);
            hah.setSoldierDieListener(this);
        }
        else
        {
            GeneralSoldierAttackHelper gsah = (GeneralSoldierAttackHelper)soldier.getAttackHelper();
            gsah.setOnSoldierDieListener(this);
            gsah.setDecampListener(this);
        }
    }

    public SoldierDrawer getDrawer()
    {
        return drawer;
    }


    public enum Status
    {
        IDLE,
        DIE,
        RUN,
        ATTACKING;

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


    private void makeAttacking()
    {
        status = Status.ATTACKING;
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

    public void moveTo(PointF dest)
    {
        makeRun();
        moveDest = dest;
        PositioningSystem ps = getDrawer().getLayer().getPosSys();
        drawer.getCurrentAnim().setScale(ps.convertX(dest) < ps.convertX(drawer.getPosition()) ? -1 : 1, 1);
    }

    private void doMoving(double deltaT)
    {
        if (status != Status.RUN)
            return;
        Point nextPoint = soldier.getAttackHelper().getPointToGo(getPoint(moveDest), deltaT);
        double distance = PointF.euclideanDistance(moveDest, drawer.getPosition());
        double stepDistance = Point.euclideanDistance(getPoint(drawer.getPosition()), nextPoint);
        //TODO‌ this kind of approach also yields the collapse with buildings except we settle on a decent and high game lopping cycle period!!
        if (distance < 0.01 || distance < stepDistance)
        {
            onMoveFinished();
            return;
        }

        drawer.setPosition(nextPoint.getX(), nextPoint.getY());
    }

    private Point getPoint(PointF pointF)
    {
        return new Point((int)round(pointF.getX()), (int)round(pointF.getY()));
    }

    private void onMoveFinished()
    {
        makeIdle();
        drawer.setPosition(moveDest.getX(), moveDest.getY());
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
        doMoving(deltaT);
    }

    @Override
    protected void callOnReload()
    {
        if (status == Status.IDLE || status == Status.ATTACKING)
        {
            makeAttacking();
            super.callOnReload();
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
                    moveTo(new PointF(newDest));
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
                    moveTo(new PointF(newDest));
                }
                else
                {
                    makeIdle();
                }
            }
        }
    }

    @Override
    public void onSoldierDie()
    {
        makeDie();
    }
}
