package menus;

import models.buildings.BuildingValues;

public class AvailableBuildingItem extends Menu
{
    private int buildingType;

    public AvailableBuildingItem(int buildingType)
    {
        super(Id.TH_AVAILABLE_BUILDING_ITEM, BuildingValues.getBuildingInfo(buildingType).getName());
        this.buildingType = buildingType;
    }

    public int getBuildingType()
    {
        return buildingType;
    }
}
