package graphics.helpers;

import models.buildings.Barracks;
import models.buildings.Building;

public class BarracksVillageHelper extends VillageHelper
{
    public BarracksVillageHelper(Building building)
    {
        super(building);
    }

    @Override
    public void onReload()
    {
        Barracks barracks = (Barracks)building;
        barracks.passTurn();
    }
}
