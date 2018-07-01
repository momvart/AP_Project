package graphics.helpers;

import graphics.drawers.SoldierDrawer;
import graphics.layers.Layer;
import models.attack.attackHelpers.GeneralSoldierAttackHelper;
import models.attack.attackHelpers.IOnDecampListener;
import models.attack.attackHelpers.IOnSoldierDieListener;
import models.soldiers.Healer;
import models.soldiers.MoveType;
import models.soldiers.Soldier;
import utils.Point;
import utils.PointF;

import java.util.List;

import static java.lang.Math.round;

public abstract class SoldierGraphicHelper extends GraphicHelper implements IOnDecampListener, IOnSoldierDieListener
{
    protected Soldier soldier;

    private SoldierDrawer drawer;

    protected PointF moveDest;
    private IOnMoveFinishedListener moveListener;
    private Status status;
    private int turn = 1;


    public SoldierGraphicHelper(Soldier soldier, Layer layer)
    {
        this.soldier = soldier;
        drawer = new SoldierDrawer(soldier);

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

    private void makeAttack()
    {
        status = Status.ATTACK;
        drawer.playAnimation(SoldierDrawer.ATTACK);
    }

    private void makeDie()
    {
        status = Status.DIE;
        //drawer.playAnimation(SoldierDrawer.DIE);
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

        drawer.setFace(dest.getX() - drawer.getPosition().getX(), dest.getY() - drawer.getPosition().getY());

        List<Point> soldierPath = soldier.getAttackHelper().getAttack().getSoldierPath(getVeryPoint(drawer.getPosition()), getVeryPoint(moveDest), soldier.getMoveType() == MoveType.AIR);
        if (soldier.getAttackHelper().getRange() != 1 && soldier.getType() != Healer.SOLDIER_TYPE)
        {
            Point lastPoint;
            lastPoint = soldier.getAttackHelper().getLastPointOfStanding(soldier.getAttackHelper().getRange(), soldier.getLocation(), getVeryPoint(moveDest));
            facingBuildingPoint = lastPoint;
            if (lastPoint.getX() > moveDest.getX())
            {
                if (lastPoint.getY() < moveDest.getY())
                    finalStandingPoint = new PointF(lastPoint.getX() - .2, lastPoint.getY() + .2);
                else if (lastPoint.getY() == moveDest.getY())
                    finalStandingPoint = new PointF(lastPoint.getX() - .8, lastPoint.getY() + .5);
                else
                    finalStandingPoint = new PointF(lastPoint.getX() - .8, lastPoint.getY() + .2);
            }
            else if (lastPoint.getX() == moveDest.getX())
            {
                if (lastPoint.getY() < moveDest.getY())
                    finalStandingPoint = new PointF(lastPoint.getX() - .5, lastPoint.getY() + .8);
                else if (lastPoint.getY() > moveDest.getY())
                    finalStandingPoint = new PointF(lastPoint.getX() - .5, lastPoint.getY() + .2);
            }
            else
            {
                if (lastPoint.getY() < moveDest.getY())
                    finalStandingPoint = new PointF(lastPoint.getX() - .2, lastPoint.getY() + .8);
                else if (lastPoint.getY() == moveDest.getY())
                    finalStandingPoint = new PointF(lastPoint.getX() - .2, lastPoint.getY() + .5);
                else
                    finalStandingPoint = new PointF(lastPoint.getX() - .2, lastPoint.getY() + .2);
            }
        }
        else
        {
            Point lastPoint = soldierPath.get(1);
            facingBuildingPoint = lastPoint;
            if (lastPoint.getX() == moveDest.getX() + 1)
            {
                if (lastPoint.getY() == moveDest.getY() - 1)
                    finalStandingPoint = new PointF(moveDest.getX() + .7, moveDest.getY() - .7);
                else if (lastPoint.getY() == moveDest.getY())
                    finalStandingPoint = new PointF(moveDest.getX() + .2, moveDest.getY() + .5);
                else
                    finalStandingPoint = new PointF(moveDest.getX() + .2, moveDest.getY() + 1.2);
            }
            else if (lastPoint.getX() == moveDest.getX())
            {
                if (lastPoint.getY() == moveDest.getY() - 1)
                    finalStandingPoint = new PointF(moveDest.getX() - .5, moveDest.getY() - .3);
                else
                    finalStandingPoint = new PointF(moveDest.getX() - .5, moveDest.getY() + 1.2);

            }
            else
            {
                if (lastPoint.getY() == moveDest.getY() - 1)
                    finalStandingPoint = new PointF(moveDest.getX() - 1.2, moveDest.getY() - .2);
                else if (lastPoint.getY() == moveDest.getY())
                    finalStandingPoint = new PointF(moveDest.getX() - 1.2, moveDest.getY() + .5);
                else
                    finalStandingPoint = new PointF(moveDest.getX() - 1.2, moveDest.getY() + 1.2);
            }
            nextCheckPointF = null;
        }
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
            nextCheckPoint = soldier.getAttackHelper().getNextPathStraightReachablePoint(getVeryPoint(drawer.getPosition()), getVeryPoint(moveDest));
            List<Point> soldierPath = soldier.getAttackHelper().getAttack().getSoldierPath(getVeryPoint(drawer.getPosition()), getVeryPoint(moveDest), soldier.getMoveType() == MoveType.AIR);
            try
            {
                if ((soldier.getAttackHelper().getRange() != 1 && soldier.getType() != Healer.SOLDIER_TYPE))
                {
                    System.out.println("archer ");
                    if (soldierPath.get(1).equals(nextCheckPoint))
                    {
                        if (PointF.euclideanDistance(drawer.getPosition(), moveDest) <= soldier.getAttackHelper().getRange())
                            onMoveFinished();
                        else
                            nextCheckPointF = finalStandingPoint;
                    }
                    else
                    {
                        nextCheckPointF = new PointF(nextCheckPoint);
                    }
                }
                else
                {
                    if (nextCheckPoint.equals(facingBuildingPoint))
                    {
                        nextCheckPointF = finalStandingPoint;
                    }
                    else
                    {
                        nextCheckPointF = new PointF(nextCheckPoint);
                    }
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

            distanceToNextCheckPoint = PointF.euclideanDistance(nextCheckPointF, drawer.getPosition());
            cos = (nextCheckPointF.getX() - drawer.getPosition().getX()) / distanceToNextCheckPoint;
            sin = (nextCheckPointF.getY() - drawer.getPosition().getY()) / distanceToNextCheckPoint;
            if (nextCheckPoint == null)
            {
                onMoveFinished();
                return;
            }
            drawer.setFace(cos, sin);
        }
        if (nextCheckPointF != null)
        {
            System.out.println("soldier position is :" + drawer.getPosition() + "finalstanding point is :" + finalStandingPoint);
            double stepDistance = deltaT * soldier.getSpeed() * 1.5;//tired of little speed of soldiers so we add a ratio to get scaped
            distanceToFinalPosition = PointF.euclideanDistance(finalStandingPoint, drawer.getPosition());
            if (distanceToFinalPosition < 0.1 || distanceToFinalPosition < stepDistance)
            {
                onMoveFinished();
                return;
            }
            PointF newPosition = new PointF(drawer.getPosition().getX() + cos * soldier.getSpeed() * deltaT, drawer.getPosition().getY() + sin * soldier.getSpeed() * deltaT);
            drawer.setPosition(newPosition.getX(), newPosition.getY());
            if (!getVeryPoint(drawer.getPosition()).equals(soldier.getLocation()))
            {
                soldier.getAttackHelper().getAttack().moveOnLocation(soldier, soldier.getLocation(), getVeryPoint(drawer.getPosition()));
                soldier.setLocation(getVeryPoint(drawer.getPosition()));
            }
        }
    }

    private Point getVeryPoint(PointF position)
    {
        return new Point((int)round(position.getX()), (int)round(position.getY()));
    }


    private void onMoveFinished()
    {
        makeAttack();
        soldier.setLocation(getVeryPoint(finalStandingPoint));
        if (moveListener != null)
            moveListener.onMoveFinished(drawer.getPosition());
        finalStandingPoint = null;
        nextCheckPointF = null;
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
    public void callOnReload()
    {
        GeneralSoldierAttackHelper gsah = (GeneralSoldierAttackHelper)soldier.getAttackHelper();
        if (turn == 1)
        {
            triggerSoldier();
            turn++;
        }
        if (status == Status.ATTACK)
        {
            makeAttack();
            if (gsah.getTarget() != null)
                System.out.println("strength is :" + gsah.getTarget().getAttackHelper().getStrength());
            super.callOnReload();
        }
    }

    public void triggerSoldier()
    {
        //implemented in the subClasses
    }

    public enum Status
    {
        DIE,
        RUN,
        ATTACK;
    }

    @Override
    public void onSoldierDie()
    {
        makeDie();
    }

    public void updateDrawer()
    {
        drawer.updateDrawer();
    }
}