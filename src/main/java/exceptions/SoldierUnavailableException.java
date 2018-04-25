package exceptions;

import models.soldiers.SoldierInfo;

public class SoldierUnavailableException extends ConsoleException
{
    public SoldierUnavailableException(SoldierInfo soldierInfo)
    {
        super("You can't build this soldier.", "Minimum level of barracks for " + soldierInfo.getName() + ": " + soldierInfo.getMinBarracksLevel());
    }
}
