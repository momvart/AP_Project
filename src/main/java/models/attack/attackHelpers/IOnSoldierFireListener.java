package models.attack.attackHelpers;

import models.buildings.BuildingDestructionReport;

public interface IOnSoldierFireListener
{
    void onSoldierFire(BuildingDestructionReport bdr);
}
