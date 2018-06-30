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

        setReloadDuration(1);
    }

    @Override
    public void onConstructFinish()
    {
        buildingDrawer.updateDrawer();
    }

    public VillageBuildingDrawer getBuildingDrawer()
    {
        return buildingDrawer;
    }
}
