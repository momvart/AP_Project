package menus;

import models.Map;
import models.World;
import models.buildings.*;

import java.util.*;

public class ShowBuildingsMenu extends Submenu
{
    public ShowBuildingsMenu(ParentMenu parent)
    {
        super(Id.VILLAGE_SHOW_BUILDINGS, "showBuildings", parent, MenuNumTextCommandHandler.getInstance());
    }

    private void setItems()
    {
        items = new ArrayList<>();
        Map map = World.sCurrentGame.getVillage().getMap();
        BuildingValues.getInfos().stream()
                .sorted(Comparator.comparing(BuildingInfo::getName))
                .forEachOrdered(info ->
                {
                    List<Building> list = map.getBuildings(info.getType()).getValues();
                    list.forEach(building -> items.add(building.getMenu(this).setShowBuildingNum(list.size() > 1)));
                });
    }

    @Override
    public ArrayList<String> getItems()
    {
        setItems();
        return super.getItems();
    }
}
