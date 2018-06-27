package graphics.helpers;

import graphics.layers.Layer;
import graphics.drawers.BuildingDrawer;
import models.Map;
import models.buildings.Building;

public abstract class BuildingGraphicHelper extends GraphicHelper
{
    public BuildingGraphicHelper(Building building, Layer layer, Map map)
    {
        setReloadDuration(0.5);
    }

    public void setUpListeners()
    {

    }

    public abstract BuildingDrawer getBuildingDrawer();
}
