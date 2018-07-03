package graphics.helpers;

import graphics.drawers.SoldierDrawer;
import graphics.layers.Layer;
import models.attack.attackHelpers.IOnDecampListener;
import models.attack.attackHelpers.IOnSoldierDieListener;
import models.attack.attackHelpers.SoldierAttackHelper;
import models.soldiers.MoveType;
import models.soldiers.Soldier;
import utils.Point;
import utils.PointF;

import java.util.List;

import static java.lang.Math.round;

public abstract class SoldierGraphicHelper extends GraphicHelper implements IOnDecampListener, IOnSoldierDieListener
{
    protected Soldier soldier;

    protected SoldierDrawer drawer;

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
        SoldierAttackHelper attackHelper = soldier.getAttackHelper();
        setMoveListener(attackHelper);
        setReloadListener(attackHelper);
        attackHelper.setDecampListener(this);
        attackHelper.setSoldierDieListener(this);
    }

    public Status getStatus()
    {
        return status;
    }

    protected PointF finalStandingPoint;
    protected double sin;
    protected double cos;

    protected void makeAttack()
    {
        status = Status.ATTACK;
        drawer.playAnimation(SoldierDrawer.ATTACK);
        PointF looking = soldier.getAttackHelper().getTargetLocation().toPointF();
        drawer.setFace(looking.getX() - drawer.getPosition().getX(), looking.getY() - drawer.getPosition().getY());
    }

    private PointF nextCheckPointF;

    protected void makeDie()
    {
        status = Status.DIE;
        //drawer.playAnimation(SoldierDrawer.DIE);
    }

    protected void makeRun()
    {
        status = Status.RUN;
        drawer.playAnimation(SoldierDrawer.RUN);
    }

    Point facingBuildingPoint;
    public void startJoggingToward(PointF dest)
    {
        makeRun();
        nextCheckPointF = null;
        moveDest = dest;
        drawer.setFace(dest.getX() - drawer.getPosition().getX(), dest.getY() - drawer.getPosition().getY());
        List<Point> soldierPath = soldier.getAttackHelper().getAttack().getSoldierPath(getVeryPoint(drawer.getPosition()), getVeryPoint(moveDest), soldier.getMoveType() == MoveType.AIR);
        if (soldierPath.size() > 1)
            setFinalStandingPoint();
        else
        {
            onMoveFinished();
            return;
        }
        facingBuildingPoint = soldierPath.get(1);
    }

    protected void setFinalStandingPoint()
    {
        Point lastPoint = soldier.getAttackHelper().getLastPointOfStanding(soldier.getAttackHelper().getRange(), soldier.getLocation(), getVeryPoint(moveDest));
        finalStandingPoint = new PointF(lastPoint);
    }

    protected void doReplacing(double deltaT)
    {
        if (status != Status.RUN)
            return;
        if (finalStandingPoint == null)
            return;
        if (nextCheckPointF == null || PointF.euclideanDistance2nd(nextCheckPointF, drawer.getPosition()) < .01)
        {
            setNewCheckPoint();
        }
        if (status == Status.RUN)
            continueMoving(deltaT);
    }

    protected void continueMoving(double deltaT)
    {
        double stepDistance = deltaT * soldier.getSpeed();
        double distanceToFinalPosition = getDistanceToFinalPosition();

        if (distanceToFinalPosition < .1 || distanceToFinalPosition < stepDistance)
        {
            onMoveFinished();
            return;
        }

        PointF newPosition;
        newPosition = new PointF(drawer.getPosition().getX() + cos * stepDistance, drawer.getPosition().getY() + sin * stepDistance);

        drawer.setPosition(newPosition.getX(), newPosition.getY());

        if (!getVeryPoint(drawer.getPosition()).equals(soldier.getLocation()))
        {
            soldier.getAttackHelper().getAttack().moveOnLocation(soldier, soldier.getLocation(), getVeryPoint(drawer.getPosition()));
            soldier.setLocation(getVeryPoint(drawer.getPosition()));
        }
    }

    protected double getDistanceToFinalPosition()
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
                if (nextCheckPoint.equals(facingBuildingPoint))
                {
                    if (isDistanceToFinalPointLessThanRange())
                    {
                        finalStandingPoint = drawer.getPosition();
                        onMoveFinished();
                        return;
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
                nextCheckPointF = new PointF(nextCheckPoint);
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

    protected boolean isDistanceToFinalPointLessThanRange()
    {
        return PointF.euclideanDistance(drawer.getPosition(), finalStandingPoint) <= soldier.getAttackHelper().getRange();
    }

    protected boolean isSoldierDistantFighter()
    {
        /*if (soldier.getType() == Healer.SOLDIER_TYPE)
            return false;*/
        return soldier.getAttackHelper().getRange() != 1;
    }

    protected Point getVeryPoint(PointF position)
    {
        return new Point((int)round(position.getX()), (int)round(position.getY()));
    }


    protected void onMoveFinished()
    {
        if (finalStandingPoint == null)
            return;
        makeAttack();
        soldier.setLocation(getVeryPoint(finalStandingPoint));
        drawer.setPosition(finalStandingPoint.getX(), finalStandingPoint.getY());
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