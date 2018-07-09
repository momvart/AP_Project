package network;

public class Message
{
    String message;
    String clientName;

    public Message(String message, String clientName)
    {
        this.message = message;
        this.clientName = clientName;
    }

    public String getClientName()
    {
        return clientName;
    }

    public String getMessage()
    {
        return message;
    }
}
