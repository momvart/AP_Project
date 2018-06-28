package graphics.helpers;

import graphics.drawers.AttackBuildingDrawer;
import graphics.layers.Layer;
import models.Map;
import models.buildings.Building;

public class AttackBuildingGraphicHelper extends BuildingGraphicHelper implements IOnDestroyListener
{
    protected AttackBuildingDrawer buildingDrawer;

    public AttackBuildingGraphicHelper(Building building, Layer layer, Map map)
    {
        super(building, layer, map);
        buildingDrawer = new AttackBuildingDrawer(building, map);
        buildingDrawer.setLayer(layer);
        buildingDrawer.setPosition(building.getLocation().getX(), building.getLocation().getY());
        buildingDrawer.updateDrawer();
    }

    @Override
    public void setUpListeners()
    {
        building.getAttackHelper().setDestroyListener(this);
    }

    @Override
    public void onDestroy()
    {
        makeDestroy();
    }

    protected void makeDestroy()
    {
        //buildingDrawer.playDestroyAnimation();
        buildingDrawer.updateDrawer();
    }

    @Override
    public void callOnReload()
    {
        System.out.println("call on reload fuckinnn in......");
        super.callOnReload();
    }

    public AttackBuildingDrawer getBuildingDrawer()
    {
        return buildingDrawer;
    }

    public void updateDrawer()
    {
        getBuildingDrawer().updateDrawer();
    }
}
