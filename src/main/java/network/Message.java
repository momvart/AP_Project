package network;

import java.util.UUID;

public class Message
{
    private String message;
    private UUID from;
    private MessageType messageType;

    public Message(String message, UUID from, MessageType messageType)
    {
        this.message = message;
        this.from = from;
        this.messageType = messageType;
    }

    public UUID getSenderId()
    {
        return from;
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
