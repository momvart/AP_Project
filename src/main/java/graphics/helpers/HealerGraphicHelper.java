package graphics.helpers;

import graphics.layers.Layer;
import models.attack.attackHelpers.HealerAttackHelper;
import models.soldiers.Soldier;
import utils.PointF;

public class HealerGraphicHelper extends SoldierGraphicHelper
{
    private HealerAttackHelper attackHelper;

    public HealerGraphicHelper(Soldier soldier, Layer layer)
    {
        super(soldier, layer);
        setReloadDuration(.3);
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
        attackHelper.setTarget(false);
        if (attackHelper.getDestination() != null)
        {
            startJoggingToward(new PointF(attackHelper.getDestination()));
        }
    }

    @Override
    public void startJoggingToward(PointF dest)
    {
        if (finalStandingPoint != null)
            return;
        makeRun();
        moveDest = dest;
        drawer.setFace(dest.getX() - drawer.getPosition().getX(), dest.getY() - drawer.getPosition().getY());
        finalStandingPoint = dest;
        if (isDistanceToFinalPointLessThanRange())
        {
            finalStandingPoint = drawer.getPosition();
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

    @Override
    protected void doReplacing(double deltaT)
    {
        if (getStatus() != Status.RUN)
            return;
        startJoggingToward(attackHelper.getDestination().toPointF());
        continueMoving(deltaT);
    }

    @Override
    public void callOnReload()
    {
        System.out.println(getStatus());
        if (attackHelper != null && getStatus() == null)
            triggerSoldier();
        if (getStatus() == Status.ATTACK)
        {
            makeAttack();
            super.callOnReload();
        }
    }


    @Override
    public void onDecamp()
    {
        if (getStatus() == Status.RUN)
            return;
        if (attackHelper.getDestination() != null)
            startJoggingToward(attackHelper.getDestination().toPointF());
    }

}
