package graphics.helpers;

import graphics.layers.Layer;
import models.Map;
import models.buildings.Building;
import models.soldiers.Soldier;
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
    protected void triggerBullet(Soldier soldier)
    {
        bulletHelper.startNewWave(new PointF(building.getLocation()), bulletUltimatePosition, soldier);
    }

    private double cos;
    private double sin;

    @Override
    public BulletHelper getBullet()
    {
        return bulletHelper;
    }

}
