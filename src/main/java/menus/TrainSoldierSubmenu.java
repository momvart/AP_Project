package menus;

import models.buildings.Barracks;
import models.buildings.Building;

import java.util.ArrayList;

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
    {}

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
