package graphics.helpers;

import models.soldiers.SoldierInjuryReport;
import utils.Point;

import java.util.ArrayList;

public interface IOnFireListener
{
    void onFire(Point targetLocation, DefenseKind defenseKind, ArrayList<SoldierInjuryReport> soldiersInjuredDirectly, ArrayList<SoldierInjuryReport> soldiersInjuredImplicitly);
}
