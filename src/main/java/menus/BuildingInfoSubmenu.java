package menus;

import models.buildings.Building;

public class BuildingInfoSubmenu extends Submenu implements IBuildingMenu
{
    public BuildingInfoSubmenu(ParentMenu parent)
    {
        super(Id.BUILDING_INFO, "Info", parent);
        insertItem(Id.OVERALL_INFO, "Overall info");
        insertItem(new BuildingUpgradeSubmenu(this));
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
}
