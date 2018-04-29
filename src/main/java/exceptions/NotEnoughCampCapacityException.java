package exceptions;

public class NotEnoughCampCapacityException extends ConsoleRuntimeException
{
    public NotEnoughCampCapacityException(String message, String datailedMessage)
    {
        super(message, datailedMessage);
    }
}
