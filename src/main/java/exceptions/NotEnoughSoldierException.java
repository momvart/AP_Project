package exceptions;

import models.soldiers.SoldierValues;

public class NotEnoughSoldierException extends ConsoleException
{
    public NotEnoughSoldierException(int soldierType, int current, int wanted)
    {
        super(String.format("Not enough %s in camps", SoldierValues.getSoldierInfo(soldierType).getName()),
                String.format("Not enough soldier. type: %d\tcurrent: %d\twanted: %d",
                        soldierType, current, wanted));
    }
}
