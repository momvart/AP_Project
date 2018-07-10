package exceptions;

import java.util.UUID;

public class GameClientNotFoundException extends ConsoleRuntimeException
{
    public GameClientNotFoundException(UUID clientId)
    {
        super("No such client.", "Client with id " + clientId + " doesn't exist.");
    }
}
