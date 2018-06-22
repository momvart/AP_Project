package models.buildings;

import exceptions.UnavailableUpgradeException;
import graphics.helpers.MineVillageHelper;
import menus.BuildingSubmenu;
import menus.Menu;
import menus.ParentMenu;
import utils.Point;

public abstract class Mine extends VillageBuilding
{
    private static final String RESOURCE_ADDPDT = "RAPDT";

    protected transient int resourceAddPerDeltaT;
    protected int minedResources;

    public Mine(Point location, int buildingNum)
    {
        super(location, buildingNum);
    }

    public int getResourceAddPerDeltaT()
    {
        return resourceAddPerDeltaT;
    }

    @Override
    public void ensureLevel()
    {
        super.ensureLevel();
        resourceAddPerDeltaT = (int)(Math.pow(1.6, level) * (double)getBuildingInfo().getMetadata(RESOURCE_ADDPDT));
    }

    @Override
    public void upgrade() throws UnavailableUpgradeException
    {
        super.upgrade();
        double newResourceAdd = resourceAddPerDeltaT * 1.6;
        resourceAddPerDeltaT = (int)newResourceAdd;
    }

    public void passTurn()
    {
        if (getBuildStatus().equals(BuildStatus.BUILT))
            minedResources += resourceAddPerDeltaT;
    }

    protected void setResourceAddPerDeltaT(int resourceAddPerDeltaT)
    {
        this.resourceAddPerDeltaT = resourceAddPerDeltaT;
    }

    public abstract void mine();

    public int getMinedResources()
    {
        return minedResources;
    }

    @Override
    public BuildingSubmenu getMenu(ParentMenu parent)
    {
        return super.getMenu(parent)
                .insertItem(Menu.Id.MINE_MINE, "Mine");
    }

    @Override
    public void createAndSetVillageHelper()
    {
        villageHelper = new MineVillageHelper(this);
    }
}
