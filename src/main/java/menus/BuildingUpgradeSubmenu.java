package menus;

import models.buildings.Building;

public class BuildingUpgradeSubmenu extends Submenu implements IBuildingMenu
{
    public BuildingUpgradeSubmenu(ParentMenu parent)
    {
        super(Id.UPGRADE_INFO, "Upgrade info", parent);
        this.clickable = true;
        insertItem(new Menu(Id.UPGRADE_COMMAND, "upgrade"));
    }

    @Override
    public Building getBuilding()
    {
        return ((IBuildingMenu)parent).getBuilding();
    }
}
