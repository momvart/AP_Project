package graphics.helpers;

import graphics.layers.Layer;
import models.Map;
import models.buildings.Building;

public class AttackBuildingGraphicHelper extends BuildingGraphicHelper implements IOnDestroyListener
{
    public AttackBuildingGraphicHelper(Building building, Layer layer, Map map)
    {
        super(building, layer, map);
    }

    @Override
    public void onDestroy()
    {
        makeDestroy();
    }

    protected void makeDestroy()
    {
        getBuildingDrawer().playDestroyAnimation();
    }

}
