package exceptions;

public class SoldierNotFoundException extends ConsoleException
{
    public SoldierNotFoundException(String message, String datailedMessage)
    {
        super(message, datailedMessage);
    }
}
