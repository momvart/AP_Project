package models.buildings;

import models.TrainingManager;
import models.Village;
import models.soldiers.Soldier;
import utils.Point;

public class Barracks extends VillageBuilding
{
    int soldierBrewTimeDecrease;
    Village village;
    TrainingManager trainingManager = new TrainingManager();

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

    public int getSoldierBrewTimeDecrease()
    {
        return soldierBrewTimeDecrease;
    }

    public void trainSoldier(int soldierType)
    {
        Soldier soldier = null;
        trainingManager.train(soldier);
    }
}