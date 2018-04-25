package models.soldiers;

import models.Village;
import models.World;
import models.soldiers.Soldier;
import models.soldiers.SoldierFactory;
import models.soldiers.SoldierInfo;
import models.soldiers.SoldierValues;

public class Recruit
{
    private int soldierType;
    private int startTurn;
    private int count;
    private int level;

    public Recruit(int soldierType, int count, int level)
    {
        this.soldierType = soldierType;
        this.startTurn = World.sCurrentGame.getVillage().getTurn();
        this.count = count;
        this.level = level;
    }

    public int getSoldierType()
    {
        return soldierType;
    }

    public SoldierInfo getSoldierInfo() { return SoldierValues.getSoldierInfo(soldierType); }

    private int getTrainTime()
    {
        return getSoldierInfo().getBrewTime();
    }

    public boolean isFinished()
    {
        return World.sCurrentGame.getVillage().getTurn() == startTurn + getTrainTime();
    }

    public int getRemainingTurns()
    {
        return getTrainTime() + startTurn - World.sCurrentGame.getVillage().getTurn();
    }

    public int getCount()
    {
        return count;
    }

    public void finishSoldier()
    {
        this.count -= 1;
        World.getVillage().getSoldiers().add(SoldierFactory.createSoldierByTypeID(soldierType, level));
    }

    public boolean checkCompleteFinish()
    {
        return count == 0;
    }

    @Override
    public String toString()
    {
        return getSoldierInfo().getName() + " " + getRemainingTurns();
    }
}
