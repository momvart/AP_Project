package graphics.helpers;

import graphics.drawers.SoldierDrawer;
import graphics.positioning.PositioningSystem;
import models.soldiers.Soldier;
import utils.PointF;

import java.net.URISyntaxException;

public class SoldierGraphicHelper extends GraphicHelper
{
    private Soldier soldier;

    private SoldierDrawer drawer;

    public SoldierGraphicHelper(Soldier soldier) throws URISyntaxException
    {
        this.soldier = soldier;
        drawer = new SoldierDrawer(soldier);
        drawer.setPosition(soldier.getLocation().getX(), soldier.getLocation().getY());
    }

    public SoldierDrawer getDrawer()
    {
        return drawer;
    }


    public enum Status
    {
        IDLE,
        MOVING,
        ATTACKING
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

    private PointF moveDest;
    private double moveLineSin, moveLineCos;
    private IOnMoveFinishedListener moveListener;

    public void moveTo(PointF dest)
    {
        status = Status.MOVING;
        moveDest = dest;
        drawer.playAnimation(SoldierDrawer.RUN);

        double distance = PointF.euclideanDistance(dest, drawer.getPosition());
        moveLineSin = (dest.getY() - drawer.getPosition().getY()) / distance;
        moveLineCos = (dest.getX() - drawer.getPosition().getX()) / distance;

        PositioningSystem ps = getDrawer().getLayer().getPosSys();
        drawer.getCurrentAnim().setScale(ps.convertX(dest) < ps.convertX(drawer.getPosition()) ? -1 : 1, 1);
    }

    private void doMoving(double deltaT)
    {
        if (status != Status.MOVING)
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

    public void attack()
    {
        status = Status.ATTACKING;
        drawer.playAnimation(SoldierDrawer.ATTACK);
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
        if (status != Status.MOVING)
            super.callOnReload();
    }
}
