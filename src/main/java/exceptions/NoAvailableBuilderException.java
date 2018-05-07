package exceptions;

public class NoAvailableBuilderException extends ConsoleException
{

    public NoAvailableBuilderException()
    {
        super("You don’t have any worker to build this building.", "No builder available");
    }
}
