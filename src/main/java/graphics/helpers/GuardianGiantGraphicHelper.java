package graphics.helpers;

import graphics.layers.Layer;
import models.Map;
import models.attack.Attack;
import models.attack.attackHelpers.GuardianGiantAttackHelper;
import models.attack.attackHelpers.IOnDecampListener;
import models.attack.attackHelpers.NetworkHelper;
import models.buildings.DefensiveTower;
import models.buildings.GuardianGiant;
import models.soldiers.Soldier;
import utils.Point;
import utils.PointF;

import java.util.List;

import static java.lang.Math.round;

public class GuardianGiantGraphicHelper extends SingleTDefenseGraphicHelper implements IOnDestroyListener, IOnDecampListener
{

    private PointF moveDest;
    private IOnMoveFinishedListener moveListener;
    private Status status;
    private PointF nextCheckPointF;
    private PointF finalStandingPoint;
    private double sin;
    private double cos;
    private Point facingBuildingPoint;
    private GuardianGiantAttackHelper attackHelper;


    public GuardianGiantGraphicHelper(GuardianGiant building, Layer layer, Map map)
    {
        super(building, layer, map);
        attackHelper = (GuardianGiantAttackHelper)building.getAttackHelper();
    }

    public void setUpListeners()
    {
        this.setReloadListener(attackHelper);
        this.setMoveListener(attackHelper);
        attackHelper.setDecampListener(this);
        attackHelper.addDestroyListener(this);
    }

    public Status getStatus()
    {
        return status;
    }

    private void makeDie()
    {
        status = Status.DIE;
        //drawer.playAnimation(SoldierDrawer.DIE);
    }

    private void makeRun()
    {
        status = Status.RUN;
        //drawer.playAnimation(SoldierDrawer.RUN);
    }

    private void doReplacing(double deltaT)
    {
        if (status != Status.RUN || finalStandingPoint == null)
        {
            return;
        }
        triggerSoldier();
        if (nextCheckPointF == null || PointF.euclideanDistance2nd(nextCheckPointF, buildingDrawer.getPosition()) < .01)
        {
            setNewCheckPoint();
        }
        if (status == Status.RUN)
            continueMoving(deltaT);
    }


    protected double getDistanceToFinalPosition()
    {
        return PointF.euclideanDistance(finalStandingPoint, buildingDrawer.getPosition());
    }

    protected Point getVeryPoint(PointF position)
    {
        return new Point((int)round(position.getX()), (int)round(position.getY()));
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
        if (status == null)
            triggerSoldier();
        if (status == Status.ATTACK)
        {
            makeAttack();
            super.callOnReload();
        }
    }

    private void triggerSoldier()
    {
        attackHelper.setTarget();
        if (attackHelper.getTargetSoldier() != null)
        {
            startJoggingToward(attackHelper.getTargetSoldier(), false);
        }
    }

    public void updateDrawer()
    {
        buildingDrawer.updateDrawer();
    }

    private void makeAttack()
    {
        status = Status.ATTACK;
        //drawer.playAnimation(SoldierDrawer.ATTACK);
        //PointF looking = attackHelper.getTargetLocation().toPointF();
        //drawer.setFace(looking.getX() - drawer.getPosition().getX(), looking.getY() - drawer.getPosition().getY());
    }

    public void startJoggingToward(Soldier targetSoldier, boolean networkPermission)
    {
        boolean isReal = attackHelper.isReal();
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
        moveDest = targetSoldier.getAttackHelper().getGraphicHelper().drawer.getPosition();
        //drawer.setFace(dest.getX() - drawer.getPosition().getX(), dest.getY() - drawer.getPosition().getY());
        List<Point> soldierPath = attackHelper.getAttack().getSoldierPath(getVeryPoint(buildingDrawer.getPosition()), getVeryPoint(moveDest), false);
        if (soldierPath.size() > 1)
            setFinalStandingPoint();
        else
        {
            onMoveFinished();
            return;
        }
        facingBuildingPoint = soldierPath.get(1);
        if (isReal)
            NetworkHelper.grdnGntStJojTowd(building.getId(), targetSoldier);
    }

    private void setFinalStandingPoint()
    {
        Point lastPoint = attackHelper.getLastPointOfStanding(((DefensiveTower)building).getRange(), building.getLocation(), getVeryPoint(moveDest));
        finalStandingPoint = new PointF(lastPoint);
    }

    private void continueMoving(double deltaT)
    {
        double stepDistance = deltaT * GuardianGiant.GUARDIAN_GIANT_SPEED;
        double distanceToFinalPosition = getDistanceToFinalPosition();

        if (attackHelper.getTargetSoldier() == null)
            standInUrPlace();
        if ((distanceToFinalPosition < .1 || distanceToFinalPosition < stepDistance) && attackHelper.getTargetSoldier().getAttackHelper().getGraphicHelper().getStatus() != SoldierGraphicHelper.Status.RUN)
        {
            onMoveFinished();
            return;
        }

        PointF newPosition;
        newPosition = new PointF(buildingDrawer.getPosition().getX() + cos * stepDistance, buildingDrawer.getPosition().getY() + sin * stepDistance);

        buildingDrawer.setPosition(newPosition.getX(), newPosition.getY());

        if (!getVeryPoint(buildingDrawer.getPosition()).equals(building.getLocation()))
            syncLogicWithGraphic();
    }

    private void standInUrPlace()
    {
        if (finalStandingPoint == null)
            return;

        syncLogicWithGraphic();
        status = null;
        if (moveListener != null)
            moveListener.onMoveFinished(buildingDrawer.getPosition());
        finalStandingPoint = null;
        nextCheckPointF = null;
        if (attackHelper.isReal())
            NetworkHelper.setGrdnGntPos(building.getId(), buildingDrawer.getPosition());
    }

    public void syncLogicWithGraphic()
    {
        Point newPoint = getVeryPoint(buildingDrawer.getPosition());
        attackHelper.getAttack().getMap().changeBuildingCell(building, newPoint);
        building.setLocation(newPoint);
    }

    private void setNewCheckPoint()
    {
        Point nextCheckPoint;
        nextCheckPoint = Attack.getNextPathStraightReachablePoint(attackHelper.getAttack(), getVeryPoint(buildingDrawer.getPosition()), getVeryPoint(moveDest), false);
        try
        {
            if (isSoldierDistantFighter())
            {
                if (nextCheckPoint.equals(facingBuildingPoint))
                {
                    if (isDistanceToFinalPointLessThanRange())
                    {
                        finalStandingPoint = buildingDrawer.getPosition();
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
        double distanceToNextCheckPoint = PointF.euclideanDistance(nextCheckPointF, buildingDrawer.getPosition());
        cos = (nextCheckPointF.getX() - buildingDrawer.getPosition().getX()) / distanceToNextCheckPoint;
        sin = (nextCheckPointF.getY() - buildingDrawer.getPosition().getY()) / distanceToNextCheckPoint;
        //drawer.setFace(cos, sin);
    }

    private boolean isDistanceToFinalPointLessThanRange()
    {
        return PointF.euclideanDistance(buildingDrawer.getPosition(), finalStandingPoint) <= ((GuardianGiant)building).getRange();
    }

    private boolean isSoldierDistantFighter()
    {
        return ((DefensiveTower)building).getRange() != 1;
    }

    public void onMoveFinished()
    {
        if (finalStandingPoint == null)
            return;
        makeAttack();

        buildingDrawer.setPosition(finalStandingPoint.getX(), finalStandingPoint.getY());
        syncLogicWithGraphic();

        if (moveListener != null)
            moveListener.onMoveFinished(buildingDrawer.getPosition());
        finalStandingPoint = null;
        nextCheckPointF = null;
        if (attackHelper.isReal())
            NetworkHelper.setGrdnGntPos(building.getId(), buildingDrawer.getPosition());
    }

    @Override
    public void onDecamp()
    {
        Soldier targetSoldier = attackHelper.getTargetSoldier();
        if (targetSoldier != null)
        {
            startJoggingToward(targetSoldier, false);
        }
        else
            status = null;
    }

    @Override
    public void onDestroy()
    {
        makeDie();
    }

    public enum Status
    {
        DIE,
        RUN,
        ATTACK;
    }
}