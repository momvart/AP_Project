package models.buildings;

public class Barracks extends VillageBuilding
{
    int soldierBrewTimeDecrease;
    TrainingManager trainingManager = new TrainingManager();

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

    public void trainSoldier(Soldier soldier)
    {

    }
}