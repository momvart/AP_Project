package menus;

import models.buildings.Building;

public class BuildingSubmenu extends Submenu implements IBuildingMenu
{
    private Building building;
    private boolean showBuildingNum;

    public BuildingSubmenu(ParentMenu parent, Building building)
    {
        this(parent, building, new BuildingInfoSubmenu(null));
    }

    public BuildingSubmenu(ParentMenu parent, Building building, BuildingInfoSubmenu infoSubmenu)
    {
        super(Id.BUILDING_MENU, building.getName() + " " + building.getBuildingNum(), parent);
        this.building = building;
        infoSubmenu.parent = this;
        insertItem(infoSubmenu);
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
