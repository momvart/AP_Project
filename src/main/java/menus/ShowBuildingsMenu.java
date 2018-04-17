package menus;

import models.World;
import models.buildings.Building;

import java.util.ArrayList;
import java.util.Comparator;

public class ShowBuildingsMenu extends Submenu
{
    public ShowBuildingsMenu(ParentMenu parent)
    {
        super(Id.VILLAGE_SHOW_BUILDINGS, "showBuildings", parent, new MenuTextCommandHandler());
    }

    private void setItems()
    {
        items = new ArrayList<>();
        World.sCurrentGame.getVillage().getBuildings().stream()
                .sorted(Comparator.comparing(Building::getName).thenComparing(Building::getBuildingNum))
                .forEachOrdered(b -> items.add(new BuildingSubmenu(this, b)));
    }

    @Override
    public ArrayList<String> getItems()
    {
        setItems();
        return super.getItems();
    }
}
