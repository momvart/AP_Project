package graphics.helpers;

import graphics.drawers.SoldierDrawer;
import graphics.layers.Layer;
import models.attack.Attack;
import models.attack.attackHelpers.GuardianGiantAttackHelper;
import models.attack.attackHelpers.NetworkHelper;
import models.buildings.GuardianGiant;
import models.soldiers.Guardian;
import utils.Point;
import utils.PointF;

import java.util.List;

public class GuardianGiantGraphicHelper extends SoldierGraphicHelper
{
    GuardianGiant guardianGiant;
    GuardianGiantAttackHelper attackHelper;

    public GuardianGiantGraphicHelper(GuardianGiant guardianGiant, Layer layer)
    {
        super(null, layer);
        this.guardianGiant = guardianGiant;

        drawer = new SoldierDrawer(new Guardian(1));
        drawer = new SoldierDrawer(new Guardian(1));
        drawer.setPosition(guardianGiant.getLocation().getX(), guardianGiant.getLocation().getY());
        drawer.setLayer(layer);

        attackHelper = (GuardianGiantAttackHelper)guardianGiant.getAttackHelper();
    }

    @Override
    protected void makeAttack()
    {
        status = Status.ATTACK;
        drawer.playAnimation(SoldierDrawer.ATTACK);
        PointF looking = attackHelper.getTargetLocation().toPointF();
        drawer.setFace(looking.getX() - drawer.getPosition().getX(), looking.getY() - drawer.getPosition().getY());
    }

    @Override
    public void startJoggingToward(PointF dest, boolean networkPermission)
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
        moveDest = dest;
        drawer.setFace(dest.getX() - drawer.getPosition().getX(), dest.getY() - drawer.getPosition().getY());
        List<Point> soldierPath = attackHelper.getAttack().getSoldierPath(getVeryPoint(drawer.getPosition()), getVeryPoint(moveDest), false);
        if (soldierPath.size() > 1)
            setFinalStandingPoint();
        else
        {
            onMoveFinished();
            return;
        }
        facingBuildingPoint = soldierPath.get(1);
        if (isReal)
            NetworkHelper.gdnGntStJojTow(guardianGiant.getId(), dest);
    }

    @Override
    protected void setFinalStandingPoint()
    {
        Point lastPoint = attackHelper.getLastPointOfStanding(guardianGiant.getRange(), guardianGiant.getLocation(), getVeryPoint(moveDest));
        finalStandingPoint = new PointF(lastPoint);
    }

    @Override
    protected void continueMoving(double deltaT)
    {
        double stepDistance = deltaT * GuardianGiant.GUARDIAN_GIANT_SPEED;
        double distanceToFinalPosition = getDistanceToFinalPosition();

        if (distanceToFinalPosition < .1 || distanceToFinalPosition < stepDistance)
        {
            onMoveFinished();
            return;
        }

        PointF newPosition;
        newPosition = new PointF(drawer.getPosition().getX() + cos * stepDistance, drawer.getPosition().getY() + sin * stepDistance);

        drawer.setPosition(newPosition.getX(), newPosition.getY());

        if (!getVeryPoint(drawer.getPosition()).equals(guardianGiant.getLocation()))
        {
            Point newPoint = getVeryPoint(drawer.getPosition());
            attackHelper.getAttack().getMap().changeBuildingCell(guardianGiant, newPoint);
            guardianGiant.setLocation(newPoint);
        }
    }

    @Override
    protected void setNewCheckPoint()
    {
        Point nextCheckPoint;
        nextCheckPoint = Attack.getNextPathStraightReachablePoint(attackHelper.getAttack(), getVeryPoint(drawer.getPosition()), getVeryPoint(moveDest), false);
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

    @Override
    protected boolean isDistanceToFinalPointLessThanRange()
    {
        return PointF.euclideanDistance(drawer.getPosition(), finalStandingPoint) <= guardianGiant.getRange();
    }

    @Override
    protected boolean isSoldierDistantFighter()
    {
        return guardianGiant.getRange() != 1;
    }

    @Override
    public void onMoveFinished()
    {
        if (finalStandingPoint == null)
            return;
        makeAttack();

        guardianGiant.setLocation(getVeryPoint(finalStandingPoint));
        attackHelper.getAttack().getMap().changeBuildingCell(guardianGiant, getVeryPoint(finalStandingPoint));

        drawer.setPosition(finalStandingPoint.getX(), finalStandingPoint.getY());
        if (moveListener != null)
            moveListener.onMoveFinished(drawer.getPosition());
        finalStandingPoint = null;
        nextCheckPointF = null;
        if (attackHelper.isReal())
            NetworkHelper.setSldPos(guardianGiant.getId(), drawer.getPosition());
    }


    @Override
    public void onDecamp()
    {
        if (attackHelper.getTargetSoldier() == null)
            return;
        Point newDest = attackHelper.getTargetLocation();
        if (newDest != null)
        {
            startJoggingToward(new PointF(newDest), false);
        }
    }
}