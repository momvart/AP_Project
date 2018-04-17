package exceptions;

public class InvalidCommandException extends ConsoleException
{
    public InvalidCommandException(String command)
    {
        super("invalid command", "invalid command: " + command);
    }

    public InvalidCommandException(String command, String details)
    {
        super("invalid command", "invalid command: " + command + '\n' + details);
    }
}
