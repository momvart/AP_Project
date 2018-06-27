package graphics.helpers;

import graphics.drawers.VillageBuildingDrawer;
import graphics.layers.Layer;
import models.Map;
import models.buildings.Building;

public class VillageBuildingGraphicHelper extends BuildingGraphicHelper implements IOnConstructFinishListener
{
    private VillageBuildingDrawer buildingDrawer;

    public VillageBuildingGraphicHelper(Building building, Layer layer, Map map)
    {
        super(building, layer, map);

        buildingDrawer = new VillageBuildingDrawer(building, map);
        buildingDrawer.setLayer(layer);
        buildingDrawer.setPosition(building.getLocation().getX(), building.getLocation().getY());
        buildingDrawer.updateDrawer();
    }

    @Override
    public void onConstructFinish()
    {
        buildingDrawer.updateDrawer();
    }

    @Override
    public VillageBuildingDrawer getBuildingDrawer()
    {
        return buildingDrawer;
    }
}
