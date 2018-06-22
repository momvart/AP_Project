package graphics.helpers;

import models.buildings.Building;
import models.buildings.Mine;

public class MineVillageHelper extends VillageHelper
{
    public MineVillageHelper(Building building)
    {
        super(building);
    }

    @Override
    public void onReload()
    {
        Mine mine = (Mine)building;
        mine.passTurn();
    }
}
