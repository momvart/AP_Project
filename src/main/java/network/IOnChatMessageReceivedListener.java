package network;

import java.util.UUID;

public interface IOnChatMessageReceivedListener
{
    void onChatMessageReceived(UUID from, String message);
}
