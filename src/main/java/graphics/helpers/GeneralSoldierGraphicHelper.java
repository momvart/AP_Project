package graphics.helpers;

import graphics.drawers.SoldierDrawer;
import graphics.layers.Layer;
import models.attack.attackHelpers.GeneralSoldierAttackHelper;
import models.attack.attackHelpers.IOnSoldierFireListener;
import models.buildings.BuildingDestructionReport;
import models.buildings.GuardianGiant;
import models.soldiers.Soldier;
import utils.Point;
import utils.PointF;

public class GeneralSoldierGraphicHelper extends SoldierGraphicHelper implements IOnSoldierFireListener
{
    GeneralSoldierAttackHelper attackHelper;

    public GeneralSoldierGraphicHelper(Soldier soldier, Layer layer)
    {
        super(soldier, layer);
        attackHelper = (GeneralSoldierAttackHelper)soldier.getAttackHelper();
    }

    @Override
    public void setUpListeners()
    {
        super.setUpListeners();
    }

    @Override
    public void triggerSoldier()
    {
        revertToDefaults();
        attackHelper.setTarget();
        if (attackHelper.getTarget() != null)
        {
            if (attackHelper.getTarget() instanceof GuardianGiant)
            {
                isChasingGuardianGiant = true;
                STOP_DISTANCE = 1;
            }
            startJoggingToward(new PointF(attackHelper.getTarget().getLocation()), false);
        }
    }

    @Override
    public void onDecamp()
    {
        revertToDefaults();
        if (attackHelper.getTarget() == null)
            return;
        Point newDest = attackHelper.getTarget().getLocation();
        if (newDest != null)
        {
            if (attackHelper.getTarget() instanceof GuardianGiant)
            {
                isChasingGuardianGiant = true;
                STOP_DISTANCE = 1;
            }
            startJoggingToward(new PointF(newDest), false);
        }
    }

    private void revertToDefaults()
    {
        isChasingGuardianGiant = false;
        STOP_DISTANCE = .1;
    }


    @Override
    public void onSoldierFire(BuildingDestructionReport report)
    {
        getDrawer().playAnimation(SoldierDrawer.ATTACK);
    }
}
