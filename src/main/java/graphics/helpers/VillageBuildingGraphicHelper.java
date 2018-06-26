package graphics.helpers;

import graphics.layers.Layer;
import models.Map;
import models.buildings.Building;

public class VillageBuildingGraphicHelper extends BuildingGraphicHelper implements IOnConstructFinishListener
{
    public VillageBuildingGraphicHelper(Building building, Layer layer, Map map)
    {
        super(building, layer, map);
    }

    @Override
    public void onConstructFinish()
    {
        getBuildingDrawer().updateDrawer();
    }

}
