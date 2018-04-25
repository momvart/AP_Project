package models;

import models.buildings.Barracks;
import models.soldiers.Recruit;
import models.soldiers.SoldierValues;

import java.util.ArrayList;

public class TrainingManager
{
    private ArrayList<Recruit> recruits = new ArrayList<>();
    private transient Barracks barracks;

    public TrainingManager()
    {

    }

    public TrainingManager(Barracks barracks)
    {
        this.barracks = barracks;
    }

    public void train(int soldierType, int count)
    {
        Recruit recruit = new Recruit(soldierType, count, barracks.getLevel());
        recruits.add(recruit);
    }

    public void passTurn()
    {
        for (int i = 0; i < recruits.size(); i++)
        {
            if (recruits.get(i).isFinished())
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
