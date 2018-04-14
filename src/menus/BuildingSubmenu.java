package menus;

import models.buildings.Building;

public class BuildingSubmenu extends Submenu implements IBuildingMenu
{
    private Building building;

    public BuildingSubmenu(ParentMenu parent, Building building)
    {
        super(Id.BUILDING_MENU, building.getName() + " " + building.getBuildingNum(), parent);
        this.building = building;
        insertItem(new BuildingInfoSubmenu(this));
    }

    public Building getBuilding()
    {
        return building;
    }
}
