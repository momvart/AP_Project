package model.buildings;

public class Barracks extends VillageBuilding
{
    int soldierBrewTimeDecrease;
    TrainingManager trainingManager = new TrainingManager();

    @Override
    public int getType()
    {
        return 0;
    }

    @Override
    public void upgrade()
    {
        super.upgrade();
    }

    public int getSoldierBrewTimeDecrease()
    {
        return soldierBrewTimeDecrease;
    }

    public void trainSoldier(Soldier soldier)
    {

    }
}