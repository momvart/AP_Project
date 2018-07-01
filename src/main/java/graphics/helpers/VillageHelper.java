package graphics.helpers;

import models.buildings.Building;

public class VillageHelper implements IOnReloadListener
{
    protected Building building;
    private VillageBuildingGraphicHelper graphicHelper;

    public VillageHelper(Building building)
    {
        this.building = building;
    }

    public VillageBuildingGraphicHelper getGraphicHelper()
    {
        return graphicHelper;
    }

    public void setGraphicHelper(VillageBuildingGraphicHelper graphicHelper)
    {
        this.graphicHelper = graphicHelper;
        this.graphicHelper.setReloadListener(this);
    }

    @Override
    public void onReload()
    {

    }
}
