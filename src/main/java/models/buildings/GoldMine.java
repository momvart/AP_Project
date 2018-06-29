package models.buildings;

import graphics.GraphicsValues;
import menus.BuildingSubmenu;
import menus.Menu;
import menus.ParentMenu;
import models.Resource;
import models.Village;
import models.World;
import utils.Point;

public class GoldMine extends Mine
{
    public GoldMine(Point location, int buildingNum)
    {
        super(location, buildingNum);
        setResourceAddPerDeltaT(10);
    }

    @Override
    public void mine()
    {
        minedResources -= World.getVillage().addResource(new Resource(minedResources, 0)).getGold();
    }

    public static final int BUILDING_TYPE = 1;

    @Override
    public int getType()
    {
        return BUILDING_TYPE;
    }

    @Override
    public BuildingSubmenu getMenu(ParentMenu parent)
    {
        return super.getMenu(parent)
                .insertItem(Menu.Id.MINE_MINE, "Mine", GraphicsValues.UI_ASSETS_PATH + "/GoldCoin.png");
    }
}
