package menus;

import models.Village;
import models.buildings.Building;

import java.util.ArrayList;

public class AvailableBuildingsSubmenu extends Submenu implements IBuildingMenu
{
    private Village village;

    public AvailableBuildingsSubmenu(ParentMenu parent, Village village)
    {
        super(Id.TH_AVAILABLE_BUILDINGS, "Available buildings", parent);
    }

    private void setItems()
    {
        //TODO: getting list of available buildings from village and set it to `items`
    }

    @Override
    public ArrayList<String> getItems()
    {
        setItems();
        return super.getItems();
    }

    @Override
    public Building getBuilding()
    {
        return ((BuildingSubmenu)parent).getBuilding();
    }
}
