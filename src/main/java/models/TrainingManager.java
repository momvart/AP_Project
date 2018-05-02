package models;

import models.buildings.Barracks;
import models.soldiers.Recruit;

import java.util.ArrayList;

public class TrainingManager
{
    private ArrayList<Recruit> recruits = new ArrayList<>();
    private long barracksId;

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
        Recruit recruit = new Recruit(soldierType, count, getBarracks().getLevel(), getBarracks().getSoldierBrewTimeDecrease());
        recruits.add(recruit);
    }

    public void passTurn()
    {
        if (recruits.size() > 0)
        {
            if (!recruits.get(0).isTraining())
                recruits.get(0).setTraining(true);
            if (recruits.get(0).getArmyQueue().size() != 0)
                recruits.get(0).addSoldiersInQueueToArmy();
            if (recruits.get(0).isCurrentFinished())
            {
                recruits.get(0).finishSoldier();
                if (recruits.get(0).checkCompleteFinish())
                {
                    recruits.remove(0);
                }
            }
        }
    }

    public ArrayList<Recruit> getRecruits()
    {
        return recruits;
    }
}
