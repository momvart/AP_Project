package menus;

import graphics.GraphicsValues;
import models.buildings.Building;

public class BuildingInfoSubmenu extends Submenu implements IBuildingMenu
{
    public BuildingInfoSubmenu(ParentMenu parent)
    {
        super(Id.BUILDING_INFO, "Info", parent);
        insertItem(Id.OVERALL_INFO, "Overall info", GraphicsValues.UI_ASSETS_PATH + "/info.png");
        insertItem(new BuildingUpgradeSubmenu(this));

        setIconPath(GraphicsValues.UI_ASSETS_PATH + "/info.png");
    }

    public Building getBuilding()
    {
        return ((BuildingSubmenu)parent).getBuilding();
    }

    @Override
    public BuildingInfoSubmenu insertItem(int id, String text)
    {
        return (BuildingInfoSubmenu)super.insertItem(id, text);
    }

    @Override
    public BuildingInfoSubmenu insertItem(int id, String text, String iconPath)
    {
        return (BuildingInfoSubmenu)super.insertItem(id, text, iconPath);
    }
}
