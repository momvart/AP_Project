package exceptions;

import models.buildings.Building;

public class UnavailableUpgradeException extends ConsoleRuntimeException
{
    public UnavailableUpgradeException(Building building)
    {
        super("Can't upgrade this building", "Can't upgrade this building" + building.getName());
    }
}
