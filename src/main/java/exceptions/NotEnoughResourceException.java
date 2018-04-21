package exceptions;

import models.Resource;

public class NotEnoughResourceException extends ConsoleException
{
    public NotEnoughResourceException(Resource current, Resource cost)
    {
        super("You don't have enough resources",
                String.format("current: %s cost: %s", current.toString(), cost.toString()));
    }
}
