package exceptions;

public class ConsoleException extends Exception
{
    protected String datailedMessage;

    public ConsoleException(String message, String datailedMessage)
    {
        super(message);
        this.datailedMessage = datailedMessage;
    }

    public String getDatailedMessage()
    {
        return datailedMessage;
    }
}
