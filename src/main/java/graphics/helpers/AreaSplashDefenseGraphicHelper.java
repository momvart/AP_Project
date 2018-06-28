package graphics.helpers;

import graphics.layers.Layer;
import models.Map;
import models.buildings.Building;
import utils.PointF;

public class AreaSplashDefenseGraphicHelper extends DefensiveTowerGraphicHelper
{

    private CannonBulletHelper bulletHelper;

    public AreaSplashDefenseGraphicHelper(Building building, Layer layer, Map map)
    {
        super(building, layer, map);
        bulletHelper = new CannonBulletHelper(this, layer);
    }

    @Override
    protected void triggerBullet()
    {
        bulletHelper.startNewWave(new PointF(building.getLocation()), bulletUltimatePosition);
    }

    @Override
    public BulletHelper getBullet()
    {
        return bulletHelper;
    }

}