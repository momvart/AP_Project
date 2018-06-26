package graphics.helpers;

import models.buildings.Building;

public class VillageHelper implements IOnReloadListener
{
    protected Building building;
    private BuildingGraphicHelper graphicHelper;

    public VillageHelper(Building building)
    {
        this.building = building;
    }

    public BuildingGraphicHelper getGraphicHelper()
    {
        return graphicHelper;
    }

    public void setGraphicHelper(BuildingGraphicHelper graphicHelper)
    {
        this.graphicHelper = graphicHelper;
        this.graphicHelper.setReloadListener(this);
    }

    @Override
    public void onReload()
    {

    }
}
