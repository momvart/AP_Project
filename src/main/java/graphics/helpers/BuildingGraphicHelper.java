package graphics.helpers;

import graphics.layers.Layer;
import models.Map;
import models.buildings.Building;

public class BuildingGraphicHelper extends GraphicHelper
{
    protected Building building;
    public BuildingGraphicHelper(Building building, Layer layer, Map map)
    {
        this.building = building;
    }

    public void setUpListeners() {}
}
