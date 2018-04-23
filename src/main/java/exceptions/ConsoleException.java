package exceptions;

public class ConsoleException extends Exception
{
    protected String datailedMessage;

    public ConsoleException()
    {

    }

    public ConsoleException(String message, String datailedMessage)
    {
        super(message);
        this.datailedMessage = datailedMessage;
    }

    public ConsoleException(String message, String datailedMessage, Throwable cause)
    {
        super(message, cause);
        this.datailedMessage = datailedMessage;
    }

    public String getDatailedMessage()
    {
        return datailedMessage;
    }
}
