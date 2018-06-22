package graphics.helpers;

import models.buildings.Building;
import models.buildings.TownHall;

public class TownHallVillageHelper extends VillageHelper
{
    public TownHallVillageHelper(Building building)
    {
        super(building);
    }

    @Override
    public void onReload()
    {
        TownHall townHall = (TownHall)building;
        townHall.passTurn();
    }
}
