package graphics.helpers;

import graphics.Layer;
import graphics.drawers.BuildingDrawer;
import models.buildings.Building;

public class BuildingGraphicHelper extends GraphicHelper implements IOnDestroyListener
{
    private Building building;

    private BuildingDrawer buildingDrawer;

    public BuildingGraphicHelper(Building building, Layer layer)
    {
        this.building = building;
        buildingDrawer = new BuildingDrawer(building);
        setReloadDuration(1.5);
        buildingDrawer.setLayer(layer);
        buildingDrawer.setPosition(building.getLocation().getX(), building.getLocation().getY());
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
