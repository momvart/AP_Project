package exceptions;

public class NotEnoughCampCapacityException extends ConsoleRuntimeException
{
    public NotEnoughCampCapacityException()
    {
        super("Not enough capacity in camp.", "Camp doesn't have more space.");
    }
}
