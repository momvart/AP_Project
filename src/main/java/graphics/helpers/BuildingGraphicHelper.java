package graphics.helpers;

import graphics.Layer;
import graphics.drawers.BuildingDrawer;
import graphics.drawers.WallDrawer;
import models.Map;
import models.buildings.Building;
import models.buildings.Wall;

public class BuildingGraphicHelper extends GraphicHelper
{
    private Building building;

    private BuildingDrawer buildingDrawer;

    public BuildingGraphicHelper(Building building, Layer layer, Map map)
    {
        this.building = building;
        buildingDrawer = building.createGraphicDrawer(map);
        setReloadDuration(1.5);
        buildingDrawer.setLayer(layer);
        buildingDrawer.setPosition(building.getLocation().getX(), building.getLocation().getY());
        buildingDrawer.setUpDrawable();
    }

    public void setUpListeners()
    {

    }


    public BuildingDrawer getBuildingDrawer()
    {
        return buildingDrawer;
    }
}
