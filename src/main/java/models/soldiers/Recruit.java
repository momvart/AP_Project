package models.soldiers;

import exceptions.NotEnoughCampCapacityException;
import exceptions.SoldierNotAddedToCampException;
import models.World;

public class Recruit
{
    private int soldierType;
    private int startTurn;
    private int count;
    private int trainedCount;
    private int level;
    private int soldierBrewTimeDecrease;
    private boolean isTraining = false;

    public Recruit(int soldierType, int count, int level, int soldierBrewTimeDecrease)
    {
        this.soldierType = soldierType;
        this.startTurn = World.sCurrentGame.getVillage().getTurn();
        this.count = count;
        this.level = level;
        this.soldierBrewTimeDecrease = soldierBrewTimeDecrease;
    }

    public int getSoldierType()
    {
        return soldierType;
    }

    public SoldierInfo getSoldierInfo() { return SoldierValues.getSoldierInfo(soldierType); }

    private int getSingleTrainTime()
    {
        return getSoldierInfo().getBrewTime() - soldierBrewTimeDecrease >= 1 ?
                getSoldierInfo().getBrewTime() - soldierBrewTimeDecrease : 1;
    }

    private int getTotalTrainTime()
    {
        return getSingleTrainTime() * count;
    }

    public boolean isCurrentFinished()
    {
        return getRemainingTurns() % (getSingleTrainTime()) == 0 &&
                World.sCurrentGame.getVillage().getTurn() != startTurn;
    }

    public int getRemainingTurns()
    {
        if (!isTraining)
            startTurn = World.getVillage().getTurn();
        return getTotalTrainTime() + startTurn - World.sCurrentGame.getVillage().getTurn();

    }

    public int getCount()
    {
        return count;
    }

    public boolean isTraining()
    {
        return isTraining;
    }

    public void setTraining(boolean training)
    {
        isTraining = training;
        startTurn = World.getVillage().getTurn() - 1;
    }

    public void finishSoldier() throws SoldierNotAddedToCampException
    {
        this.trainedCount += 1;
        try
        {
            World.getVillage().addSoldier(SoldierFactory.createSoldierByTypeID(soldierType, level));
        }
        catch (NotEnoughCampCapacityException ex)
        {
            throw new SoldierNotAddedToCampException("soldier not added to the camp", "SoldierNotAddedToCamp"
                    , SoldierFactory.createSoldierByTypeID(soldierType, level));
        }


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
