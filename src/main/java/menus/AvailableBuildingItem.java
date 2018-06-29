package menus;

import graphics.GraphicsValues;
import models.buildings.BuildingInfo;
import models.buildings.BuildingValues;

public class AvailableBuildingItem extends Menu
{
    private BuildingInfo buildingInfo;

    public AvailableBuildingItem(BuildingInfo buildingInfo)
    {
        super(Id.TH_AVAILABLE_BUILDING_ITEM, buildingInfo.getName());
        this.buildingInfo = buildingInfo;
        setIconPath(GraphicsValues.getBuildingAssetsPath(buildingInfo.getType()) + "/000/001.png");
    }

    public BuildingInfo getBuildingInfo()
    {
        return buildingInfo;
    }
}
