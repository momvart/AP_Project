package menus;

import models.buildings.Building;

public class BuildingSubmenu extends Submenu implements IBuildingMenu
{
    private Building building;
    private boolean showBuildingNum;

    public BuildingSubmenu(ParentMenu parent, Building building)
    {
        super(Id.BUILDING_MENU, building.getName() + " " + building.getBuildingNum(), parent);
        this.building = building;
        insertItem(new BuildingInfoSubmenu(this));
    }

    @Override
    public String getText()
    {
        if (showBuildingNum)
            return super.getText();
        else
            return building.getName();
    }

    public BuildingSubmenu setShowBuildingNum(boolean showBuildingNum)
    {
        this.showBuildingNum = showBuildingNum;
        return this;
    }

    public Building getBuilding()
    {
        return building;
    }
}
