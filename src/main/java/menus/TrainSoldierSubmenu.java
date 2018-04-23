package menus;

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
    private int availableElixir;

    public TrainSoldierSubmenu(ParentMenu parent, Barracks barracks, int availableElixir)
    {
        super(Id.BARRACKS_TRAIN_SOLDIER, "Build soldiers", parent);
        this.barracks = barracks;
        this.availableElixir = availableElixir;
    }

    private void setItems()
    {
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
