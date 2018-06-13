package graphics.helpers;

import graphics.drawers.SoldierDrawer;
import graphics.positioning.PositioningSystem;
import models.attack.attackHelpers.*;
import models.soldiers.Healer;
import models.soldiers.Soldier;
import utils.Point;
import utils.PointF;

import java.net.URISyntaxException;

public class SoldierGraphicHelper extends GraphicHelper implements IOnDecampListener, IOnSoldierDieListener
{
    protected Soldier soldier;

    private SoldierDrawer drawer;

    protected PointF moveDest;
    private double moveLineSin, moveLineCos;
    private boolean isSoldierHealer = false;

    private IOnMoveFinishedListener moveListener;


    public SoldierGraphicHelper(Soldier soldier) throws URISyntaxException
    {
        this.soldier = soldier;
        drawer = new SoldierDrawer(soldier);
        drawer.setPosition(soldier.getLocation().getX(), soldier.getLocation().getY());
        if (soldier.getType() == Healer.SOLDIER_TYPE)
        {
            isSoldierHealer = true;
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

        double distance = PointF.euclideanDistance(dest, drawer.getPosition());
        moveLineSin = (dest.getY() - drawer.getPosition().getY()) / distance;
        moveLineCos = (dest.getX() - drawer.getPosition().getX()) / distance;

        PositioningSystem ps = getDrawer().getLayer().getPosSys();
        drawer.getCurrentAnim().setScale(ps.convertX(dest) < ps.convertX(drawer.getPosition()) ? -1 : 1, 1);
    }

    private void doMoving(double deltaT)
    {
        if (status != Status.RUN)
            return;

        double distance = PointF.euclideanDistance(moveDest, drawer.getPosition());
        double step = soldier.getSoldierInfo().getSpeed() * deltaT;

        if (distance < 0.01 || distance < step)
        {
            onMoveFinished();
            return;
        }

        drawer.setPosition(drawer.getPosition().getX() + step * moveLineCos,
                drawer.getPosition().getY() + step * moveLineSin);
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
        if (soldier.getAttackHelper().isDead())
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
