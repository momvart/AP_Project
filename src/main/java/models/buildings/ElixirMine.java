package models.buildings;

import graphics.GraphicsValues;
import menus.BuildingSubmenu;
import menus.Menu;
import menus.ParentMenu;
import models.Resource;
import models.World;
import utils.Point;

public class ElixirMine extends Mine
{
    public ElixirMine(Point location, int buildingNum)
    {
        super(location, buildingNum);
        setResourceAddPerDeltaT(5);
    }

    @Override
    public void mine()
    {
        minedResources -= World.getVillage().addResource(new Resource(0, minedResources)).getGold();
    }

    public static final int BUILDING_TYPE = 2;

    @Override
    public int getType()
    {
        return BUILDING_TYPE;
    }

    @Override
    public BuildingSubmenu getMenu(ParentMenu parent)
    {
        return super.getMenu(parent)
                .insertItem(Menu.Id.MINE_MINE, "Mine", GraphicsValues.UI_ASSETS_PATH + "/elixir.png");
    }
}
