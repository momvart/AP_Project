package graphics.helpers;

import graphics.layers.Layer;
import models.Map;
import models.buildings.Building;

public class AreaSplashDefenseGraphicHelper extends DefensiveTowerGraphicHelper
{

    public AreaSplashDefenseGraphicHelper(Building building, Layer layer, Map map)
    {
        super(building, layer, map);
    }

    private boolean isFirstTime = true;
    private CannonBulletHelper helper = new CannonBulletHelper();

}
