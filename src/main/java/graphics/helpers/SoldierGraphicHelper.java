package graphics.helpers;

import graphics.layers.Layer;
import graphics.drawers.SoldierDrawer;
import graphics.positioning.PositioningSystem;
import models.attack.attackHelpers.GeneralSoldierAttackHelper;
import models.attack.attackHelpers.IOnDecampListener;
import models.attack.attackHelpers.IOnSoldierDieListener;
import models.soldiers.MoveType;
import models.soldiers.Soldier;
import utils.Point;
import utils.PointF;

import java.net.URISyntaxException;
import java.util.List;

import static java.lang.Math.floor;

public abstract class SoldierGraphicHelper extends GraphicHelper implements IOnDecampListener, IOnSoldierDieListener
{
    protected Soldier soldier;

    private SoldierDrawer drawer;

    protected PointF moveDest;
    private boolean isSoldierHealer = false;
    private IOnMoveFinishedListener moveListener;
    private Status status;
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

        setReloadDuration(.7);

        drawer.setPosition(soldier.getLocation().getX(), soldier.getLocation().getY());
        drawer.setLayer(layer);
    }

    public SoldierDrawer getDrawer()
    {
        return drawer;
    }

    public void setUpListeners()
    {
    }

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
        status = Status.ATTACK;
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

    private PointF finalStandingPoint;
    private Point facingBuildingPoint;
    private PointF nextCheckPointF;
    private double cos;
    private double sin;

    public void startJoggingToward(PointF dest)
    {
        makeRun();
        moveDest = dest;
        PositioningSystem ps = getDrawer().getLayer().getPosSys();
        drawer.getCurrentAnim().setScale(ps.convertX(dest) < ps.convertX(drawer.getPosition()) ? -1 : 1, 1);
        List<Point> soldierPath = soldier.getAttackHelper().getAttack().getSoldierPath(getVeryPoint(drawer.getPosition()), getVeryPoint(moveDest), soldier.getMoveType() == MoveType.AIR);
        Point lastPoint = soldierPath.get(1);
        facingBuildingPoint = lastPoint;
        if (lastPoint.getX() == moveDest.getX() + 1)
        {
            if (lastPoint.getY() == moveDest.getY() - 1)
                finalStandingPoint = moveDest;
            else if (lastPoint.getY() == moveDest.getY())
                finalStandingPoint = new PointF(moveDest.getX(), moveDest.getY() + .5);
            else
                finalStandingPoint = new PointF(moveDest.getX(), moveDest.getY() + 1);
        }
        else if (lastPoint.getX() == moveDest.getX())
        {
            if (lastPoint.getY() == moveDest.getY() - 1)
                finalStandingPoint = new PointF(moveDest.getX() - .5, moveDest.getY());
            else
                finalStandingPoint = new PointF(moveDest.getX() - .5, moveDest.getY() + 1);

        }
        else
        {
            if (lastPoint.getY() == moveDest.getY() - 1)
                finalStandingPoint = new PointF(moveDest.getX() - 1, moveDest.getY());
            else if (lastPoint.getY() == moveDest.getY())
                finalStandingPoint = new PointF(moveDest.getX() - 1, moveDest.getY() + .5);
            else
                finalStandingPoint = new PointF(moveDest.getX() - 1, moveDest.getY() + 1);
        }
        nextCheckPointF = null;


    }

    private void doReplacing(double deltaT)
    {
        Point nextCheckPoint;
        if (status != Status.RUN)
            return;
        double distanceToNextCheckPoint;
        double distanceToFinalPosition;
        if (nextCheckPointF == null || PointF.euclideanDistance(nextCheckPointF, drawer.getPosition()) < .1)
        {
            nextCheckPoint = soldier.getAttackHelper().getNextPathStarightReachablePoint(getVeryPoint(drawer.getPosition()), getVeryPoint(moveDest));
            nextCheckPointF = new PointF(nextCheckPoint);
            System.out.println("facing building section is :" + facingBuildingPoint);
            if (nextCheckPoint.equals(facingBuildingPoint) && PointF.euclideanDistance(drawer.getPosition(), new PointF(facingBuildingPoint)) < .1)
            {
                nextCheckPointF = finalStandingPoint;
                System.out.println("here");
            }
            System.out.println("position is :" + drawer.getPosition());
            System.out.println("next checkPoint is : " + nextCheckPoint);
            distanceToNextCheckPoint = PointF.euclideanDistance(nextCheckPointF, drawer.getPosition());
            cos = (nextCheckPointF.getX() - drawer.getPosition().getX()) / distanceToNextCheckPoint;
            sin = (nextCheckPointF.getY() - drawer.getPosition().getY()) / distanceToNextCheckPoint;
            if (nextCheckPoint == null)
            {
                onMoveFinished();
                return;
            }
        }
        System.out.println("next checkPointF is :‌" + nextCheckPointF);
        System.out.println("final standing point is : " + finalStandingPoint);
        System.out.println(drawer.getPosition());
        System.out.println("cos and sin are :" + cos + "   " + sin);

        if (nextCheckPointF != null)
        {
            double stepDistance = deltaT * soldier.getSpeed() * 1.5;//tired of little speed of soldiers so we add a ratio to get scaped
            distanceToFinalPosition = PointF.euclideanDistance(finalStandingPoint, drawer.getPosition());
            System.out.println("distance till end " + distanceToFinalPosition);
            if (distanceToFinalPosition < 0.1 || distanceToFinalPosition < stepDistance)
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
    }

    private Point getVeryPoint(PointF position)
    {
        return new Point((int)floor(position.getX()), (int)floor(position.getY()));
    }



    private void onMoveFinished()
    {
        makeIdle();
        drawer.setPosition(finalStandingPoint.getX(), finalStandingPoint.getY());
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

    public void triggerSoldier()
    {
        //implemented in the subClasses
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
