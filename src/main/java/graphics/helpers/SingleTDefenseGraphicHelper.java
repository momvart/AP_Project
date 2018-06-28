package graphics.helpers;

import graphics.layers.Layer;
import models.Map;
import models.buildings.Building;
import utils.PointF;

public class SingleTDefenseGraphicHelper extends DefensiveTowerGraphicHelper
{
    private BulletHelper bulletHelper;

    public SingleTDefenseGraphicHelper(Building building, Layer layer, Map map)
    {
        super(building, layer, map);
        bulletHelper = new BulletHelper(this, layer);
    }

    @Override
    protected void triggerBullet()
    {
        bulletHelper.startNewWave(new PointF(building.getLocation()), bulletUltimatePosition);
    }

    private double cos;
    private double sin;

    @Override
    public BulletHelper getBullet()
    {
        return bulletHelper;
    }

}
