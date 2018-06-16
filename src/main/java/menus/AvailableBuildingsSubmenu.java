package menus;

import models.Resource;
import models.Village;
import models.World;
import models.buildings.Building;
import models.buildings.BuildingInfo;
import models.buildings.BuildingValues;
import models.buildings.TownHall;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.stream.Collectors;

public class AvailableBuildingsSubmenu extends Submenu implements IBuildingMenu
{
    public AvailableBuildingsSubmenu(ParentMenu parent)
    {
        super(Id.TH_AVAILABLE_BUILDINGS, "Available buildings", parent);
    }

    private void setItems()
    {
        Resource availableResource = World.sCurrentGame.getVillage().getResources();
        items = BuildingValues.getInfos().stream()
                .filter(info -> info.getType() != TownHall.BUILDING_TYPE)
                .filter(info -> info.getBuildCost().isLessThanOrEqual(availableResource))
                .sorted(Comparator.comparing(BuildingInfo::getName))
                .map(AvailableBuildingItem::new)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public ArrayList<String> getItems()
    {
        setItems();
        return super.getItems();
    }

    @Override
    public ArrayList<Menu> getMenuItems()
    {
        setItems();
        return super.getMenuItems();
    }

    @Override
    public Building getBuilding()
    {
        return ((BuildingSubmenu)parent).getBuilding();
    }
}
