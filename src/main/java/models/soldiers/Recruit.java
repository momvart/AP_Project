package models.soldiers;

import exceptions.NotEnoughCampCapacityException;
import exceptions.SoldierNotAddedToCampException;
import models.World;

public class Recruit
{
    private int soldierType;
    private int remainingTurn;
    private int level;
    private int soldierBrewTimeDecrease;
    private boolean isTraining = false;

    public Recruit(int soldierType, int level, int soldierBrewTimeDecrease)
    {
        this.soldierType = soldierType;
        this.level = level;
        this.soldierBrewTimeDecrease = soldierBrewTimeDecrease;
        remainingTurn = getSingleTrainTime();
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

    public boolean isCurrentFinished()
    {
        return remainingTurn == 0;
    }

    public int getRemainingTurns()
    {
        return remainingTurn;
    }

    public boolean isTraining()
    {
        return isTraining;
    }

    public void setTraining(boolean training)
    {
        isTraining = training;
    }

    public void finishSoldier() throws SoldierNotAddedToCampException
    {
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

    public void passTurn()
    {
        if (remainingTurn > 0)
            remainingTurn--;

    }

    @Override
    public String toString()
    {
        return String.format("%s %d", getSoldierInfo().getName(), getRemainingTurns());
    }
}
