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
        for (int i = 0; i < recruits.size(); i++)
        {
            if (recruits.get(i).getArmyQueue().size() != 0)
            {
                recruits.get(i).addSoldiersInQueueToArmy();
            }
            if (recruits.get(i).isCurrentFinished())
            {
                recruits.get(i).finishSoldier();
                if (recruits.get(i).checkCompleteFinish())
                {
                    recruits.remove(i);
                    i--;
                }
            }
        }
    }

    public ArrayList<Recruit> getRecruits()
    {
        return recruits;
    }
}
