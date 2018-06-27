package models;

import exceptions.NotEnoughCampCapacityException;
import exceptions.SoldierNotAddedToCampException;
import models.buildings.Barracks;
import models.soldiers.Recruit;
import models.soldiers.Soldier;
import models.soldiers.SoldierFactory;

import java.util.ArrayList;

public class TrainingManager
{
    private ArrayList<Recruit> recruits = new ArrayList<>();
    private long barracksId;
    private ArrayList<Soldier> armyQueue = new ArrayList<>();

    public TrainingManager()
    {

    }

    public TrainingManager(Barracks barracks)
    {
        this.barracksId = barracks.getId();
        this.cachedBarracks = barracks;
    }

    private transient Barracks cachedBarracks;

    public Barracks getBarracks()
    {
        if (cachedBarracks == null)
            cachedBarracks = World.getVillage().getMap().getBuildingById(barracksId);
        return cachedBarracks;
    }

    public void train(int soldierType, int count)
    {
        for (int i = 1; i <= count; i++)
        {
            Recruit recruit = new Recruit(soldierType, getBarracks().getLevel(), getBarracks().getSoldierBrewTimeDecrease());
            recruits.add(recruit);
        }
    }

    public void passTurn()
    {
        if (recruits.size() > 0)
        {
            if (!recruits.get(0).isTraining())
                recruits.get(0).setTraining(true);
            if (armyQueue.size() != 0)
                addSoldiersInQueueToArmy();
            recruits.get(0).passTurn();
            if (recruits.get(0).isCurrentFinished())
            {
                try
                {
                    recruits.get(0).finishSoldier();
                    recruits.remove(0);
                }
                catch (SoldierNotAddedToCampException ex)
                {
                    armyQueue.add(ex.getSoldier());
                    recruits.remove(0);
                }
            }
        }
    }

    private void addSoldiersInQueueToArmy()
    {
        while (armyQueue.size() != 0)
        {
            try
            {
                World.getVillage().addSoldier(SoldierFactory.createSoldierByTypeID(armyQueue.get(0).getSoldierInfo().getType(), armyQueue.get(0).getLevel()));
                armyQueue.remove(0);
            }
            catch (NotEnoughCampCapacityException ex)
            {
                break;
            }
        }
    }

    public ArrayList<Recruit> getRecruits()
    {
        return recruits;
    }
}
