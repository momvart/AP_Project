package network;

import java.util.UUID;

public class Message
{
    private String message;
    private UUID from;
    private MessageType messageType;
    private String metadata;

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

    public String getMetadata()
    {
        return metadata;
    }

    public Message setMetadata(String metadata)
    {
        this.metadata = metadata;
        return this;
    }

    @Override
    public String toString()
    {
        return String.format("From: %s\nMessageType: %s\nMessage: %s\nMetadata: %s",
                from,
                messageType.name(),
                message,
                metadata);
    }
}
