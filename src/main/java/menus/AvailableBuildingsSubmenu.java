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
    private Village village;

    public AvailableBuildingsSubmenu(ParentMenu parent, Village village)
    {
        super(Id.TH_AVAILABLE_BUILDINGS, "Available buildings", parent);
        this.village = village;
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
    public Building getBuilding()
    {
        return ((BuildingSubmenu)parent).getBuilding();
    }
}
