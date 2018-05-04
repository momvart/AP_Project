package exceptions;

import models.buildings.Building;

public class BuildingInConstructionException extends ConsoleException
{
    public BuildingInConstructionException(Building building)
    {
        super("You can't take this action because building is in construction", "You can't take this action because building is in construction");
    }
}
