package graphics.helpers;

import graphics.layers.Layer;
import models.Map;
import models.buildings.Building;
import models.soldiers.Soldier;
import utils.PointF;

public class AreaSplashDefenseGraphicHelper extends DefensiveTowerGraphicHelper
{

    private BulletHelper bulletHelper;

    public AreaSplashDefenseGraphicHelper(Building building, Layer layer, Map map)
    {
        super(building, layer, map);
        bulletHelper = new CannonBulletHelper(this, layer);
    }

    @Override
    protected void triggerBullet(Soldier soldier)
    {
        bulletHelper.startNewWave(new PointF(building.getLocation()), new PointF(bulletUltimatePosition), soldier);
    }

    @Override
    public BulletHelper getBullet()
    {
        return bulletHelper;
    }

}