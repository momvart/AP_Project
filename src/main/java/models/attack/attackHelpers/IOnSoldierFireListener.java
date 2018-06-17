package models.attack.attackHelpers;

import models.buildings.BuildingDestructionReport;
import utils.Point;

public interface IOnSoldierFireListener
{
    void onSoldierFire(Point locationOfTarget, BuildingDestructionReport bdr);
}
