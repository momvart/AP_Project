package models;

import models.buildings.Recruit;
import models.soldiers.Soldier;

import java.util.ArrayList;

public class TrainingManager
{
    ArrayList<Recruit> recruits;
    Village village;

    public void train(Soldier soldier)
    {
        Recruit recruit = new Recruit(soldier.getType(), soldier.getArmyUnitInfo().getBrewTime(), soldier);
        recruits.add(recruit);
    }

    public void checkTraining()
    {
        for (int i = 0; i < recruits.size(); i++)
        {
            if (recruits.get(i).isFinished())
            {
                village.getSoldiers().add(recruits.get(i).getSoldier());
                recruits.remove(i);
                i--;
            }
        }
    }
    public ArrayList<Recruit> getRecruits()
    {
        return recruits;
    }
}
