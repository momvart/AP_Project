package exceptions;

import models.buildings.Building;

public class UnavailableUpgradeException extends ConsoleRuntimeException
{
    public enum Reason
    {
        IMPOSSIBLE,
        REACHED_TH
    }

    public UnavailableUpgradeException(Building building)
    {
        this(building, Reason.IMPOSSIBLE);
    }

    public UnavailableUpgradeException(Building building, Reason reason)
    {
        super("Can't upgrade this building", "Can't upgrade " + building.getName() + "because " +
                (reason == Reason.IMPOSSIBLE ? "it is not upgradable at all." : "it has reached the town hall level."));
    }
}
