package models.buildings;

import exceptions.NotEnoughResourceException;
import menus.*;
import models.Resource;
import models.TrainingManager;
import models.World;
import models.soldiers.SoldierValues;
import utils.Point;

public class Barracks extends VillageBuilding
{
    private int soldierBrewTimeDecrease;
    private TrainingManager trainingManager;

    public Barracks(Point location, int buildingNum)
    {
        super(location, buildingNum);
        trainingManager = new TrainingManager(this);
    }

    public void passTurn()
    {
        trainingManager.passTurn();
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
        soldierBrewTimeDecrease++;
    }

    public TrainingManager getTrainingManager()
    {
        return trainingManager;
    }

    public int getSoldierBrewTimeDecrease()
    {
        return soldierBrewTimeDecrease;
    }

    public void trainSoldier(int soldierType, int count) throws NotEnoughResourceException
    {
        World.getVillage().spendResource(Resource.multiply(SoldierValues.getSoldierInfo(soldierType).getBrewCost(), count));
        trainingManager.train(soldierType, count);
    }

    @Override
    public BuildingSubmenu getMenu(ParentMenu parent)
    {
        BuildingSubmenu menu = super.getMenu(parent);
        menu.insertItem(new TrainSoldierSubmenu(menu, this))
                .insertItem(new Menu(Menu.Id.BARRACKS_STATUS, "Status"));
        return menu;
    }
}