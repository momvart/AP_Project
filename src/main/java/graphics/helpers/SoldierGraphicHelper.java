package graphics.helpers;

import graphics.drawers.SoldierDrawer;
import graphics.layers.Layer;
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
    private PointF nextCheckPointF;
    private double cos;
    private double sin;
    private Point byTheBuildingPoint;

    public void startJoggingToward(PointF dest)
    {
        makeRun();
        moveDest = dest;
        drawer.setFace(dest.getX() - drawer.getPosition().getX(), dest.getY() - drawer.getPosition().getY());
        List<Point> soldierPath = soldier.getAttackHelper().getAttack().getSoldierPath(getVeryPoint(drawer.getPosition()), getVeryPoint(moveDest), soldier.getMoveType() == MoveType.AIR);
        if (soldierPath.size() > 1)
            byTheBuildingPoint = soldierPath.get(1);
        else
            byTheBuildingPoint = null;
        setFinalStandingPoint();
    }

    private void setFinalStandingPoint()
    {
        if (isSoldierDistantFighter())
        {
            Point lastPoint = soldier.getAttackHelper().getLastPointOfStanding(soldier.getAttackHelper().getRange(), soldier.getLocation(), getVeryPoint(moveDest));
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
            if (byTheBuildingPoint == null)
            {
                finalStandingPoint = moveDest;
            }
            else
            {
                if (byTheBuildingPoint.getX() == moveDest.getX() + 1)
                {
                    if (byTheBuildingPoint.getY() == moveDest.getY() - 1)
                        finalStandingPoint = new PointF(moveDest.getX() + .7, moveDest.getY() - .7);
                    else if (byTheBuildingPoint.getY() == moveDest.getY())
                        finalStandingPoint = new PointF(moveDest.getX() + .2, moveDest.getY() + .5);
                    else
                        finalStandingPoint = new PointF(moveDest.getX() + .2, moveDest.getY() + 1.2);
                }
                else if (byTheBuildingPoint.getX() == moveDest.getX())
                {
                    if (byTheBuildingPoint.getY() == moveDest.getY() - 1)
                        finalStandingPoint = new PointF(moveDest.getX() - .5, moveDest.getY() - .3);
                    else
                        finalStandingPoint = new PointF(moveDest.getX() - .5, moveDest.getY() + 1.2);

                }
                else
                {
                    if (byTheBuildingPoint.getY() == moveDest.getY() - 1)
                        finalStandingPoint = new PointF(moveDest.getX() - 1.2, moveDest.getY() - .2);
                    else if (byTheBuildingPoint.getY() == moveDest.getY())
                        finalStandingPoint = new PointF(moveDest.getX() - 1.2, moveDest.getY() + .5);
                    else
                        finalStandingPoint = new PointF(moveDest.getX() - 1.2, moveDest.getY() + 1.2);
                }
            }
            nextCheckPointF = null;
        }
    }

    private void doReplacing(double deltaT)
    {
        if (status != Status.RUN)
            return;
        if (finalStandingPoint == null)
            return;
        if (nextCheckPointF == null || PointF.euclideanDistance2nd(nextCheckPointF, drawer.getPosition()) < .01)
        {
            setNewCheckPoint();
        }
        continueMoving(deltaT);
    }

    private void continueMoving(double deltaT)
    {
        double stepDistance = deltaT * soldier.getSpeed();
        double distanceToFinalPosition = getDistanceToFinalPosition();

        if (distanceToFinalPosition < 0.1 || distanceToFinalPosition < stepDistance)
        {
            onMoveFinished();
            return;
        }
        /*else
            System.out.println("df " + distanceToFinalPosition + " " + stepDistance);
        System.out.println("final standing point is :" + finalStandingPoint+  "current position is :‌ "+ drawer.getPosition());
        */
        PointF newPosition;
        newPosition = new PointF(drawer.getPosition().getX() + cos * stepDistance, drawer.getPosition().getY() + sin * stepDistance);

        drawer.setPosition(newPosition.getX(), newPosition.getY());
        if (!getVeryPoint(drawer.getPosition()).equals(soldier.getLocation()))
        {
            soldier.getAttackHelper().getAttack().moveOnLocation(soldier, soldier.getLocation(), getVeryPoint(drawer.getPosition()));
            soldier.setLocation(getVeryPoint(drawer.getPosition()));
        }
    }

    private double getDistanceToFinalPosition()
    {
        return PointF.euclideanDistance(finalStandingPoint, drawer.getPosition());
    }

    private void setNewCheckPoint()
    {
        Point nextCheckPoint;
        nextCheckPoint = soldier.getAttackHelper().getNextPathStraightReachablePoint(getVeryPoint(drawer.getPosition()), getVeryPoint(moveDest));
        try
        {
            if (isSoldierDistantFighter())
            {
                if (nextCheckPoint.equals(byTheBuildingPoint))
                {
                    if (isDistanceToFinalPointLessThanRange())
                    {
                        finalStandingPoint = drawer.getPosition();
                        onMoveFinished();
                    }
                    else
                    {
                        nextCheckPointF = finalStandingPoint;
                    }
                }
                else
                {
                    nextCheckPointF = new PointF(nextCheckPoint);
                }
            }
            else
            {
                if (nextCheckPoint.equals(byTheBuildingPoint))
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

        if (nextCheckPointF == null)
        {
            onMoveFinished();
            return;
        }
        double distanceToNextCheckPoint = PointF.euclideanDistance(nextCheckPointF, drawer.getPosition());
        cos = (nextCheckPointF.getX() - drawer.getPosition().getX()) / distanceToNextCheckPoint;
        sin = (nextCheckPointF.getY() - drawer.getPosition().getY()) / distanceToNextCheckPoint;
        drawer.setFace(cos, sin);
    }

    private boolean isDistanceToFinalPointLessThanRange()
    {
        return PointF.euclideanDistance(drawer.getPosition(), finalStandingPoint) <= soldier.getAttackHelper().getRange();
    }

    private boolean isSoldierDistantFighter()
    {
        if (soldier.getType() == Healer.SOLDIER_TYPE)
            return false;
        return soldier.getAttackHelper().getRange() != 1;
    }

    private Point getVeryPoint(PointF position)
    {
        return new Point((int)round(position.getX()), (int)round(position.getY()));
    }


    private void onMoveFinished()
    {
        makeAttack();
        soldier.setLocation(getVeryPoint(finalStandingPoint));
        drawer.setPosition(finalStandingPoint.getX(), finalStandingPoint.getY());
        if (moveListener != null)
            moveListener.onMoveFinished(drawer.getPosition());
        finalStandingPoint = null;
        nextCheckPointF = null;
        byTheBuildingPoint = null;
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
        if (turn == 1)
        {
            triggerSoldier();
            turn++;
        }
        if (status == Status.ATTACK)
        {
            makeAttack();
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