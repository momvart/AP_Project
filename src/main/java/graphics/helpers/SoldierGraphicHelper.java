package graphics.helpers;

import graphics.drawers.SoldierDrawer;
import graphics.layers.Layer;
import models.attack.Attack;
import models.attack.attackHelpers.IOnDecampListener;
import models.attack.attackHelpers.IOnSoldierDieListener;
import models.attack.attackHelpers.NetworkHelper;
import models.attack.attackHelpers.SoldierAttackHelper;
import models.soldiers.MoveType;
import models.soldiers.Soldier;
import utils.Point;
import utils.PointF;

import java.util.List;

import static java.lang.Math.round;

public abstract class SoldierGraphicHelper extends GraphicHelper implements IOnDecampListener, IOnSoldierDieListener
{

    protected double STOP_DISTANCE = .1;
    protected Soldier soldier;

    protected SoldierDrawer drawer;

    protected PointF moveDest;
    protected IOnMoveFinishedListener moveListener;
    protected Status status;
    private int turn = 1;
    private SoldierAttackHelper attackHelper;
    protected PointF nextCheckPointF;
    protected boolean isChasingGuardianGiant = false;

    public SoldierDrawer getDrawer()
    {
        return drawer;
    }

    public void setUpListeners()
    {
        this.setReloadListener(attackHelper);
        this.setMoveListener(attackHelper);
        attackHelper.setDecampListener(this);
        attackHelper.addSoldierDieListener(this);
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

    public SoldierGraphicHelper(Soldier soldier, Layer layer)
    {
        this.soldier = soldier;
        drawer = new SoldierDrawer(soldier);
        setReloadDuration(.7);
        if (drawer == null)
            return;
        drawer.setPosition(soldier.getLocation().getX(), soldier.getLocation().getY());
        drawer.setLayer(layer);
        attackHelper = soldier.getAttackHelper();
    }

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

    public void startJoggingToward(PointF dest, boolean networkPermission)
    {
        boolean isReal = soldier.getAttackHelper().isReal();
        if (!isReal && !networkPermission)
        {
            moveDest = null;
            nextCheckPointF = null;
            facingBuildingPoint = null;
            finalStandingPoint = null;
            return;
        }

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
        if (isReal)
            NetworkHelper.sldrStJogTowd(soldier.getId(), dest);
    }


    protected void setFinalStandingPoint()
    {
        Point lastPoint = Attack.getLastPointOfStanding(attackHelper.getAttack(), soldier.getAttackHelper().getRange(), soldier.getLocation(), getVeryPoint(moveDest), soldier.getMoveType() == MoveType.AIR);
        finalStandingPoint = new PointF(lastPoint);
    }

    protected void doReplacing(double deltaT)
    {
        if (status != Status.RUN || finalStandingPoint == null || moveDest == null)
            return;
        if (isChasingGuardianGiant)
            triggerSoldier();

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

        if (distanceToFinalPosition < STOP_DISTANCE || distanceToFinalPosition < stepDistance)
        {
            onMoveFinished();
            return;
        }

        PointF newPosition;
        newPosition = new PointF(drawer.getPosition().getX() + cos * stepDistance, drawer.getPosition().getY() + sin * stepDistance);

        drawer.setPosition(newPosition.getX(), newPosition.getY());

        if (!getVeryPoint(drawer.getPosition()).equals(soldier.getLocation()))
        {
            syncLogicWithGraphic();
        }
    }

    public void syncLogicWithGraphic()
    {
        soldier.getAttackHelper().getAttack().moveOnLocation(soldier, soldier.getLocation(), getVeryPoint(drawer.getPosition()));
        soldier.setLocation(getVeryPoint(drawer.getPosition()));
    }

    protected double getDistanceToFinalPosition()
    {
        return PointF.euclideanDistance(finalStandingPoint, drawer.getPosition());
    }

    protected void setNewCheckPoint()
    {
        Point nextCheckPoint;
        nextCheckPoint = Attack.getNextPathStraightReachablePoint(attackHelper.getAttack(), getVeryPoint(drawer.getPosition()), getVeryPoint(moveDest), soldier.getMoveType() == MoveType.AIR);
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
        return soldier.getAttackHelper().getRange() != 1;
    }

    protected Point getVeryPoint(PointF position)
    {
        return new Point((int)round(position.getX()), (int)round(position.getY()));
    }


    public void onMoveFinished()
    {
        if (finalStandingPoint == null)
            return;
        makeAttack();

        drawer.setPosition(finalStandingPoint.getX(), finalStandingPoint.getY());
        syncLogicWithGraphic();

        if (moveListener != null)
            moveListener.onMoveFinished(drawer.getPosition());
        finalStandingPoint = null;
        nextCheckPointF = null;
        if (soldier.getAttackHelper().isReal())
            NetworkHelper.setSldPos(soldier.getId(), drawer.getPosition());
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