package menus;

import models.buildings.BuildingInfo;
import models.buildings.BuildingValues;

public class AvailableBuildingItem extends Menu
{
    private BuildingInfo buildingInfo;

    public AvailableBuildingItem(BuildingInfo buildingInfo)
    {
        super(Id.TH_AVAILABLE_BUILDING_ITEM, buildingInfo.getName());
        this.buildingInfo = buildingInfo;
    }

    public BuildingInfo getBuildingInfo()
    {
        return buildingInfo;
    }
}
