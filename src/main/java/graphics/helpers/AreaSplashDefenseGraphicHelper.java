package graphics.helpers;

import graphics.Layer;
import models.Map;
import models.buildings.Building;
import utils.PointF;

public class AreaSplashDefenseGraphicHelper extends DefensiveTowerGraphicHelper
{
    public AreaSplashDefenseGraphicHelper(Building building, Layer layer, Map map)
    {
        super(building, layer, map);
    }

    @Override
    protected void bulletFlyContinue()
    {

        if (currentState == State.FIRING)
        {
            if (bulletUltimatePosition != null)
            {
                if (PointF.euclideanDistance(bulletDrawer.getPosition(), bulletUltimatePosition) < 0.01)
                {
                    hasBulletHitTarget = true;
                    bulletHitListener.onBulletHit();
                    return;
                }


            }
        }
    }
}
