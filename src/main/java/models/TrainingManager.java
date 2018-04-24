package models;

import models.buildings.Barracks;
import models.buildings.Recruit;
import models.soldiers.SoldierValues;

import java.util.ArrayList;

public class TrainingManager
{
    private ArrayList<Recruit> recruits = new ArrayList<>();
    private transient Village village = World.getVillage();
    private transient Barracks barracks;

    public void train(int soldierType, int count)
    {
        Recruit recruit = new Recruit(soldierType, SoldierValues.getSoldierInfo(soldierType).getBrewTime(), count, barracks.getLevel());
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
