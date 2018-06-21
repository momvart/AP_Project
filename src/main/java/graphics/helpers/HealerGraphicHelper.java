package graphics.helpers;

import graphics.Layer;
import graphics.drawers.SoldierDrawer;
import models.attack.attackHelpers.HealerAttackHelper;
import models.attack.attackHelpers.IOnHealerHealListener;
import models.soldiers.Soldier;
import models.soldiers.SoldiersHealReport;
import utils.Point;
import utils.PointF;

import java.util.ArrayList;

public class HealerGraphicHelper extends SoldierGraphicHelper implements IOnHealerHealListener
{
    HealerAttackHelper attackHelper;

    public HealerGraphicHelper(Soldier soldier, Layer layer)
    {
        super(soldier, layer);
        attackHelper = (HealerAttackHelper)soldier.getAttackHelper();
    }

    @Override
    public void setUpListeners()
    {
        super.setUpListeners();
    }

    @Override
    public void triggerSoldier()
    {
        attackHelper = (HealerAttackHelper)soldier.getAttackHelper();
        attackHelper.setTarget();
        if (attackHelper.getDestination() != null)
        {
            startJoggingToward(new PointF(attackHelper.getDestination()));
        }
    }

    @Override
    public void onDecamp()
    {
        Point newDest = attackHelper.getDestination();
        if (newDest != null)
        {
            startJoggingToward(new PointF(newDest));
        }
        else
        {
            makeIdle();
        }
    }

    @Override
    public void onHeal(ArrayList<SoldiersHealReport> reports)
    {
        getDrawer().playAnimation(SoldierDrawer.ATTACK);
        for (SoldiersHealReport report : reports)
        {
            SoldierDrawer soldierDrawer = report.getSoldier().getAttackHelper().getGraphicHelper().getDrawer();
            soldierDrawer.beeingHealedGlow();
            soldierDrawer.healthIncreaseBarLoading(report.getInitialHealth(), report.getFinalHealth());
        }
    }
}
