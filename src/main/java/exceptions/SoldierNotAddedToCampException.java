package exceptions;

import models.soldiers.Soldier;

public class SoldierNotAddedToCampException extends ConsoleException
{
    private Soldier soldier;

    public SoldierNotAddedToCampException(String message, String datailedMessage, Soldier soldier)
    {
        super(message, datailedMessage);
        this.soldier = soldier;
    }

    public Soldier getSoldier()
    {
        return soldier;
    }
}
