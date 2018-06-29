package menus;

import graphics.GraphicsValues;
import models.buildings.Building;

public class BuildingUpgradeSubmenu extends Submenu implements IBuildingMenu
{
    public BuildingUpgradeSubmenu(ParentMenu parent)
    {
        super(Id.UPGRADE_INFO, "Upgrade info", parent);
        this.clickable = true;
        insertItem(new Menu(Id.UPGRADE_COMMAND, "upgrade", GraphicsValues.UI_ASSETS_PATH + "/build.png"));
        setIconPath(GraphicsValues.UI_ASSETS_PATH + "/build.png");
    }

    @Override
    public Building getBuilding()
    {
        return ((IBuildingMenu)parent).getBuilding();
    }
}
