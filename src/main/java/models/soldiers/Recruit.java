package models.soldiers;

import exceptions.NotEnoughCampCapacityException;
import models.World;

import java.util.ArrayList;

public class Recruit
{
    private int soldierType;
    private int startTurn;
    private int count;
    private int trainedCount;
    private int level;
    private int soldierBrewTimeDecrease;
    private ArrayList<Soldier> armyQueue = new ArrayList<>();

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

    public ArrayList getArmyQueue()
    {
        return armyQueue;
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
        return getTotalTrainTime() + startTurn - World.sCurrentGame.getVillage().getTurn();
    }

    public int getCount()
    {
        return count;
    }

    public void finishSoldier()
    {
        this.trainedCount += 1;
        try
        {
            World.getVillage().addSoldier(SoldierFactory.createSoldierByTypeID(soldierType, level));
        }
        catch (NotEnoughCampCapacityException ex)
        {
            armyQueue.add(SoldierFactory.createSoldierByTypeID(soldierType, level));
        }

    }

    public void addSoldiersInQueueToArmy()
    {
        while (armyQueue.size() != 0)
        {
            try
            {
                World.getVillage().addSoldier(armyQueue.get(0));
                armyQueue.remove(0);
            }
            catch (NotEnoughCampCapacityException ex)
            {
                break;
            }
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
