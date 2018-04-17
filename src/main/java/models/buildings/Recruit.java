package models.buildings;

import models.Village;
import models.World;
import models.soldiers.Soldier;
import models.soldiers.SoldierInfo;
import models.soldiers.SoldierValues;

public class Recruit
{
    private int soldierType;
    private int startTurn;
    private int trainTime;
    private Soldier soldier;

    public Recruit(int soldierType, int trainTime, Soldier soldier)
    {
        this.soldierType = soldierType;
        this.startTurn = World.sCurrentGame.getVillage().getTurn();
        this.trainTime = trainTime;
        this.soldier = soldier;
    }

    public int getSoldierType()
    {
        return soldierType;
    }

    public SoldierInfo getSoldierInfo() {return SoldierValues.getSoldierInfo(soldierType);}

    public boolean isFinished()
    {
        return World.sCurrentGame.getVillage().getTurn() == startTurn + trainTime;
    }

    public int getRemainingTurns()
    {
        return trainTime + startTurn - World.sCurrentGame.getVillage().getTurn();
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
