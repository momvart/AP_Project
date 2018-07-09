package network;

public class Message
{
    String message;
    String clientName;
    MessageType messageType;

    public Message(String message, String clientName, MessageType messageType)
    {
        this.message = message;
        this.clientName = clientName;
        this.messageType = messageType;
    }

    public String getClientName()
    {
        return clientName;
    }

    public String getMessage()
    {
        return message;
    }

    public MessageType getMessageType()
    {
        return messageType;
    }
}
