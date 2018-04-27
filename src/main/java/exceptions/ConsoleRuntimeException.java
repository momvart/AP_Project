package exceptions;

public class ConsoleRuntimeException extends RuntimeException
{
    protected String datailedMessage;

    public ConsoleRuntimeException()
    {

    }

    public ConsoleRuntimeException(String message, String datailedMessage)
    {
        super(message);
        this.datailedMessage = datailedMessage;
    }

    public ConsoleRuntimeException(String message, String datailedMessage, Throwable cause)
    {
        super(message, cause);
        this.datailedMessage = datailedMessage;
    }

    public String getDatailedMessage()
    {
        return datailedMessage;
    }
}
