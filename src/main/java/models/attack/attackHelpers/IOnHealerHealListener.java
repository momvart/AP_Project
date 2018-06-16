package models.attack.attackHelpers;

import models.soldiers.SoldiersHealReport;

import java.util.ArrayList;

public interface IOnHealerHealListener
{
    void onHeal(ArrayList<SoldiersHealReport> reports);
}
