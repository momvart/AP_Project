package models.buildings;

import menus.Menu;
import menus.ParentMenu;
import menus.Submenu;
import menus.TrainSoldierSubmenu;
import models.TrainingManager;
import models.Village;
import models.World;
import models.soldiers.Soldier;
import models.soldiers.SoldierFactory;
import utils.Point;

public class Barracks extends VillageBuilding
{
    private int soldierBrewTimeDecrease;
    private TrainingManager trainingManager = new TrainingManager();

    public Barracks(Point location)
    {
        super(location);
    }

    @Override
    public int getType()
    {
        return 6;
    }

    @Override
    public void upgrade()
    {
        super.upgrade();
        if (soldierBrewTimeDecrease > 0)
            soldierBrewTimeDecrease--;
    }

    public TrainingManager getTrainingManager()
    {
        return trainingManager;
    }

    public int getSoldierBrewTimeDecrease()
    {
        return soldierBrewTimeDecrease;
    }

    public void trainSoldier(int soldierType)
    {
        Soldier soldier = SoldierFactory.createSoldierByTypeID(soldierType, this.level);
        trainingManager.train(soldier);
    }

    @Override
    public Submenu getMenu(ParentMenu parent)
    {
        Submenu menu = super.getMenu(parent);
        menu.insertItem(new TrainSoldierSubmenu(menu, this, World.getVillage().getResources().getElixir()))
                .insertItem(new Menu(Menu.Id.BARRACKS_STATUS, "Status"));
        return menu;
    }
}