package models.buildings;

import exceptions.UnavailableUpgradeException;
import menus.BuildingInfoSubmenu;
import menus.BuildingSubmenu;
import menus.Menu;
import menus.ParentMenu;
import utils.Point;

public class Camp extends VillageBuilding
{
    private int capacity;

    public Camp(Point location, int buildingNum)
    {
        super(location, buildingNum);
        capacity = (int)(double)getBuildingInfo().getMetadata("initialCapacity");
    }


    public int getCapacity()
    {
        return capacity;
    }

    public static final int BUILDING_TYPE = 7;

    @Override
    public int getType()
    {
        return BUILDING_TYPE;
    }

    @Override
    public void upgrade() throws UnavailableUpgradeException
    {
        throw new UnavailableUpgradeException(this, UnavailableUpgradeException.Reason.IMPOSSIBLE);
    }

    @Override
    public BuildingInfoSubmenu getInfoSubmenu()
    {
        return new BuildingInfoSubmenu(null)
                .insertItem(Menu.Id.CAMP_CAPACITY_INFO, "Capacity Info");
    }

    @Override
    public BuildingSubmenu getMenu(ParentMenu parent)
    {
        return super.getMenu(parent)
                .insertItem(Menu.Id.CAMP_SOLDIERS, "Soldiers");
    }
}