package exceptions;

public class InvalidCommandException extends ConsoleException
{
    public InvalidCommandException(String command)
    {
        super("invalid command", "invalid command: " + command);
    }
}
