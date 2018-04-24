package models;

import models.buildings.Barracks;
import models.buildings.Recruit;
import models.soldiers.Soldier;

import java.util.ArrayList;

public class TrainingManager
{
    private ArrayList<Recruit> recruits = new ArrayList<>();
    private transient Barracks barracks;

    public void train(Soldier soldier)
    {
        Recruit recruit = new Recruit(soldier.getType(), soldier.getSoldierInfo().getBrewTime(), soldier);
        recruits.add(recruit);
    }

    public void checkTraining()
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
