package models.buildings;


import menus.BuildingInfoSubmenu;
import menus.Menu;
import utils.Point;

public abstract class Storage extends VillageBuilding
{
    int capacity;

    public Storage(Point location, int buildingNum)
    {
        super(location, buildingNum);
    }

    public int getCapacity()
    {
        return capacity;
    }

    @Override
    public void upgrade()
    {
        super.upgrade();
        double newCapacity = capacity * 1.6;
        capacity = (int)newCapacity;
    }

    @Override
    public BuildingInfoSubmenu getInfoSubmenu()
    {
        return (BuildingInfoSubmenu)new BuildingInfoSubmenu(null)
                .insertItem(Menu.Id.STORAGE_SRC_INFO, "Sources info");
    }
}
