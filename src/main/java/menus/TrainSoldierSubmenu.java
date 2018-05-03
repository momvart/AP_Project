package menus;

import models.World;
import models.buildings.Barracks;
import models.buildings.Building;
import models.soldiers.SoldierInfo;
import models.soldiers.SoldierValues;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.stream.Collectors;

public class TrainSoldierSubmenu extends Submenu implements IBuildingMenu
{
    private Barracks barracks;

    public TrainSoldierSubmenu(ParentMenu parent, Barracks barracks)
    {
        super(Id.BARRACKS_TRAIN_SOLDIER, "Build Soldiers", parent);
        this.barracks = barracks;
    }

    private void setItems()
    {
        int availableElixir = World.getVillage().getResources().getElixir();
        items = SoldierValues.getInfos().stream()
                .sorted(Comparator.comparingInt(SoldierInfo::getType))
                .map(info -> new TrainSoldierItem(info.getType(), info.getMinBarracksLevel() > barracks.getLevel() ? -1 : availableElixir / info.getBrewCost().getElixir()))
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
