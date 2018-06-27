package graphics.helpers;

import graphics.layers.Layer;
import models.Map;
import models.buildings.Building;

public class SingleTDefenseGraphicHelper extends DefensiveTowerGraphicHelper
{
    public SingleTDefenseGraphicHelper(Building building, Layer layer, Map map)
    {
        super(building, layer, map);
    }

    private double cos;
    private double sin;
    private boolean isFirstTime = true;
    private BulletHelper helper = new BulletHelper();
}
