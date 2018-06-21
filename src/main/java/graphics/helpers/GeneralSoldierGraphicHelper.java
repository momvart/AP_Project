package graphics.helpers;

import graphics.Layer;
import graphics.drawers.SoldierDrawer;
import models.attack.attackHelpers.GeneralSoldierAttackHelper;
import models.attack.attackHelpers.IOnSoldierFireListener;
import models.buildings.BuildingDestructionReport;
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
        attackHelper.setTarget();
        if (attackHelper.getTarget() != null)
        {
            startJoggingToward(new PointF(attackHelper.getTarget().getLocation()));
        }
    }

    @Override
    public void onDecamp()
    {
        Point newDest = attackHelper.getTarget().getLocation();
        if (newDest != null)
        {
            startJoggingToward(new PointF(newDest));
        }
        else
            makeIdle();
    }

    @Override
    public void onSoldierFire(BuildingDestructionReport report)
    {
        getDrawer().playAnimation(SoldierDrawer.ATTACK);
        report.getBuilding().getAttackHelper().getGraphicHelper().getBuildingDrawer().healthDecreseBarLoading(report.getInitialStrength(), report.getFinalStrength());
    }
}
