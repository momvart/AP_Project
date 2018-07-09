package network;

import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

//GameClient that is used in client side
public class GameClientC extends GameClient
{
    private HashMap<UUID, ClientInfo> players;

    private IOnChatMessageReceivedListener chatMessageReceiver;

    public GameClientC(int port, String ip) throws IOException
    {
        super(new Socket(ip, port));
    }

    private void callOnChatMessageReceived(Message chatMessage)
    {
        if (chatMessageReceiver != null)
            chatMessageReceiver.onChatMessageReceived(players.get(chatMessage.getSenderId()).getName(), chatMessage.getMessage());
    }

    public void setChatMessageReceiver(IOnChatMessageReceivedListener chatMessageReceiver)
    {
        this.chatMessageReceiver = chatMessageReceiver;
    }

    @Override
    public void messageReceived(Message message)
    {
        switch (message.getMessageType())
        {
            case SET_ID:
                this.info.setId(UUID.fromString(message.getMessage()));
                break;
            case PLAYERS_LIST:
                players = new HashMap<>();
                ((ArrayList<ClientInfo>)deserializer.fromJson(message.getMessage(), new TypeToken<ArrayList<ClientInfo>>() { }.getType())).forEach(info -> players.put(info.getId(), info));
                break;
            case CHAT_MESSAGE:
                callOnChatMessageReceived(message);
                break;
            case ATTACK_REQUEST:
                break;
            default:
                super.messageReceived(message);
        }
    }

    public void sendChatMessage(String message)
    {
        sendMessage(new Message(message, getClientId(), MessageType.CHAT_MESSAGE));
    }
}
