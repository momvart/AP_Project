package models.buildings;

import models.Village;
import models.soldiers.Soldier;
import models.soldiers.SoldierInfo;
import models.soldiers.SoldierValues;

public class Recruit
{
    private int soldierType;
    private int startTurn;
    private int trainTime;
    private Soldier soldier;
    private Village village;

    public Recruit(int soldierType, int trainTime, Soldier soldier, Village village)
    {
        this.soldierType = soldierType;
        this.startTurn = village.getTurn();
        this.trainTime = trainTime;
        this.soldier = soldier;
        this.village = village;
    }

    public int getSoldierType()
    {
        return soldierType;
    }

    public SoldierInfo getSoldierInfo() {return SoldierValues.getSoldierInfo(soldierType);}

    public boolean isFinished()
    {
        return village.getTurn() == startTurn + trainTime;
    }

    public int getRemainingTurns()
    {
        return trainTime + startTurn - village.getTurn();
    }

    public Soldier getSoldier()
    {
        return soldier;
    }

    @Override
    public String toString()
    {
        return getSoldierInfo().getName() + " " + getRemainingTurns();
    }
}
