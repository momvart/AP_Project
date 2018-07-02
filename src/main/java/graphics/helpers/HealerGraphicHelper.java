package graphics.helpers;

import graphics.layers.Layer;
import models.attack.attackHelpers.HealerAttackHelper;
import models.soldiers.Soldier;
import utils.PointF;

public class HealerGraphicHelper extends SoldierGraphicHelper
{
    HealerAttackHelper attackHelper;

    public HealerGraphicHelper(Soldier soldier, Layer layer)
    {
        super(soldier, layer);
        setReloadDuration(.7);
    }

    @Override
    public void setUpListeners()
    {
        super.setUpListeners();
        attackHelper = (HealerAttackHelper)soldier.getAttackHelper();
    }

    @Override
    public void triggerSoldier()
    {
        attackHelper.setTarget();
        if (attackHelper.getDestination() != null)
        {
            startJoggingToward(new PointF(attackHelper.getDestination()));
        }
    }

    @Override
    public void startJoggingToward(PointF dest)
    {
        makeRun();
        moveDest = dest;
        drawer.setFace(dest.getX() - drawer.getPosition().getX(), dest.getY() - drawer.getPosition().getY());
        finalStandingPoint = getFinalStandingPoint(dest);
        if (isDistanceToFinalPointLessThanRange())
        {
            onMoveFinished();
            return;
        }
        else
        {
            double distanceToFinalPosition = getDistanceToFinalPosition();
            cos = (finalStandingPoint.getX() - drawer.getPosition().getX()) / distanceToFinalPosition;
            sin = (finalStandingPoint.getY() - drawer.getPosition().getY()) / distanceToFinalPosition;
        }
    }

    private PointF getFinalStandingPoint(PointF dest)
    {
        return soldier.getAttackHelper().getLastPointOfStanding(soldier.getAttackHelper().getRange(), soldier.getLocation(), getVeryPoint(dest)).toPointF();
    }

    @Override
    protected void doReplacing(double deltaT)
    {
        /*
        if (getStatus() != Status.RUN)
            return;
        if (finalStandingPoint == null)
            return;
        if (attackHelper != null && attackHelper.getDestination() != null)
            continueMoving(deltaT);
    */
    }

    @Override
    public void callOnReload()
    {
        if (attackHelper != null && getStatus() == null)
            triggerSoldier();
        super.callOnReload();
        if (attackHelper != null && attackHelper.getDestination() != null)
            startJoggingToward(new PointF(attackHelper.getDestination()));
    }

    @Override
    public void onDecamp()
    {
        // meaning less decamp for healers
    }

}
