package exceptions;

public class BuilderNotFoundException extends ConsoleRuntimeException
{
    public BuilderNotFoundException(String message, String datailedMessage)
    {
        super(message, datailedMessage);
    }
}
