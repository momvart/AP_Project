package graphics.helpers;

import graphics.layers.Layer;
import models.attack.attackHelpers.HealerAttackHelper;
import models.soldiers.Soldier;
import utils.Point;
import utils.PointF;

public class HealerGraphicHelper extends SoldierGraphicHelper
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
    }

}
