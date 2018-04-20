package exceptions;

public class NoAvailableBuilderException extends ConsoleException
{

    public NoAvailableBuilderException(String message, String datailedMessage)
    {
        super(message, datailedMessage);
    }
}
