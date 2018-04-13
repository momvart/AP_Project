package models.buildings;

import models.soldiers.Soldier;
import models.soldiers.SoldierInfo;
import models.soldiers.SoldierValues;

public class Recruit
{
    int soldierType;
    int startTurn;
    int trainTime;
    Soldier soldier;

    public int getSoldierType()
    {
        return soldierType;
    }

    public SoldierInfo getSoldierInfo() {return SoldierValues.getSoldierInfo(soldierType);}

    public boolean isFinished()
    {

    }

    public int getRemainingTurns()
    {
        return 0;
    }

    private void addToArmy()
    {

    }

    @Override
    public String toString()
    {
        return getSoldierInfo().getName() + " " + getRemainingTurns();
    }
}
