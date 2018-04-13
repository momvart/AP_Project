package menus;

import models.buildings.Barracks;

import java.util.ArrayList;

public class TrainSoldierSubmenu extends Submenu
{
    private Barracks barracks;
    private int availableElixir;

    public TrainSoldierSubmenu(ParentMenu parent, Barracks barracks, int availableElixir)
    {
        super(Id.BARRACKS_BUILD_SOLDIER, "Build soldiers", parent);
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
}
