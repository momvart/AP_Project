package graphics.helpers;

import graphics.drawers.VillageBuildingDrawer;
import graphics.layers.Layer;
import models.Map;
import models.World;
import models.buildings.Building;
import models.buildings.Wall;

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
        if (building.getType() == Wall.BUILDING_TYPE)
        {
            Building otherWall;
            try
            {
                if ((otherWall = World.getVillage().getMap().getBuildingAt(building.getLocation().getX(), building.getLocation().getY() + 1)).getType() == Wall.BUILDING_TYPE)
                    otherWall.getVillageHelper().getGraphicHelper().getBuildingDrawer().updateDrawer();
            }
            catch (Exception ignored) {}
            try
            {
                if ((otherWall = World.getVillage().getMap().getBuildingAt(building.getLocation().getX() - 1, building.getLocation().getY())).getType() == Wall.BUILDING_TYPE)
                    otherWall.getVillageHelper().getGraphicHelper().getBuildingDrawer().updateDrawer();
            }
            catch (Exception ignored) {}
        }
    }

    public VillageBuildingDrawer getBuildingDrawer()
    {
        return buildingDrawer;
    }
}
