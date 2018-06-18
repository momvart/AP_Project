package graphics.helpers;

import graphics.Layer;
import graphics.drawers.BuildingDrawer;
import graphics.drawers.WallDrawer;
import models.Map;
import models.buildings.Building;
import models.buildings.Wall;

public class BuildingGraphicHelper extends GraphicHelper implements IOnDestroyListener
{
    private Building building;

    private BuildingDrawer buildingDrawer;

    public BuildingGraphicHelper(Building building, Layer layer, Map map)
    {
        this.building = building;
        if (building.getType() == Wall.BUILDING_TYPE)
            buildingDrawer = new WallDrawer((Wall)building, map);
        buildingDrawer = new BuildingDrawer(building);
        setReloadDuration(1.5);
        buildingDrawer.setLayer(layer);
        buildingDrawer.setPosition(building.getLocation().getX(), building.getLocation().getY());
        buildingDrawer.setUpDrawable();
    }

    @Override
    public void onDestroy()
    {
        makeDestroy();
    }


    public void setUpListeners()
    {
        building.getAttackHelper().setDestroyListener(this);
    }

    public void makeDestroy()
    {
        buildingDrawer.playDestroyAnimation();
    }

    public BuildingDrawer getBuildingDrawer()
    {
        return buildingDrawer;
    }
}
