package menus;

import model.buildings.Building;

public class BuildingInfoSubmenu extends Submenu
{
    public BuildingInfoSubmenu(ParentMenu parent)
    {
        super(Id.BUILDING_INFO, "Info", parent);
        parent.insertItem(Id.OVERALL_INFO, "Overall info")
                .insertItem(Id.UPGRADE_INFO, "Upgrade info");
    }

    public Building getBuilding()
    {
        return ((BuildingSubmenu)parent).getBuilding();
    }
}
