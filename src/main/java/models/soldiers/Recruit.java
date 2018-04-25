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
    private int trainedCount;
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

    private int getSingleTrainTime() {return getSoldierInfo().getBrewTime();}

    private int getTotalTrainTime()
    {
        return getSingleTrainTime() * count;
    }

    public boolean isCurrentFinished()
    {
        return getRemainingTurns() % getSingleTrainTime() == 0 &&
                World.sCurrentGame.getVillage().getTurn() != startTurn;
    }

    public int getRemainingTurns()
    {
        return getTotalTrainTime() + startTurn - World.sCurrentGame.getVillage().getTurn();
    }

    public int getCount()
    {
        return count;
    }

    public void finishSoldier()
    {
        this.trainedCount += 1;
        World.getVillage().getSoldiers().add(SoldierFactory.createSoldierByTypeID(soldierType, level));
    }

    public boolean checkCompleteFinish()
    {
        return count == trainedCount;
    }

    @Override
    public String toString()
    {
        return String.format("%s x%d: %d", getSoldierInfo().getName(), count - trainedCount, getRemainingTurns());
    }
}
