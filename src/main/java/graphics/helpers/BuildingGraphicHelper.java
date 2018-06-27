package graphics.helpers;

import graphics.drawers.BuildingDrawer;
import graphics.layers.Layer;
import models.Map;
import models.buildings.Building;

public class BuildingGraphicHelper extends GraphicHelper
{
    protected Building building;

    protected BuildingDrawer buildingDrawer;

    public BuildingGraphicHelper(Building building, Layer layer, Map map)
    {
        this.building = building;
        this.buildingDrawer = new BuildingDrawer(building, map);
        setReloadDuration(0.5);
        buildingDrawer.setLayer(layer);
        buildingDrawer.setPosition(building.getLocation().getX(), building.getLocation().getY());
    }

    public void setUpListeners()
    {

    }


    public BuildingDrawer getBuildingDrawer()
    {
        return buildingDrawer;
    }
}
