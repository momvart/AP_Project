package exceptions;

public class NoAvailableBuilderException extends ConsoleException
{

    public NoAvailableBuilderException()
    {
        //TODO: add some parameters for detailed message
        super("You don’t have any worker to build this building.", "No builder available");
    }
}
