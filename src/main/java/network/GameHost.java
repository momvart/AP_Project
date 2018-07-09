package network;

import com.google.gson.Gson;

import java.io.*;
import java.net.ServerSocket;
import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public class GameHost extends Thread implements IOnMessageReceivedListener
{
    private ServerSocket serverSocket;

    private HashMap<UUID, GameClient> clients = new HashMap<>();

    private Gson gson = new Gson();

    public GameHost(int port) throws IOException
    {
        this.serverSocket = new ServerSocket(port);
    }

    @Override
    public void run()
    {
        try
        {
            while (true)
            {
                GameClient client = new GameClient(serverSocket.accept());
                clients.put(client.getClientId(), client);
                client.setUp();
                client.sendMessage(new Message(client.getClientId().toString(), null, MessageType.SET_ID));
                client.setMessageReceiver(this);
                broadcastClientsList();
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    //send client message to other clients
    public void broadcastMessage(Message message)
    {
        clients.values().forEach(client -> client.sendMessage(message));
    }

    public void broadcastExcept(UUID exception, Message message)
    {
        clients.values().stream().filter(client -> !client.getClientId().equals(exception)).forEach(client -> client.sendMessage(message));
    }

    private void broadcastClientsList()
    {
        broadcastMessage(new Message(gson.toJson(clients.values().stream().map(GameClient::getInfo).collect(Collectors.toList())), null, MessageType.PLAYERS_LIST));
    }

    //send message to specific client
    public void sendMessage(UUID clientId, Message message)
    {
        clients.get(clientId).sendMessage(message);
    }

    @Override
    public void messageReceived(Message message)
    {
        switch (message.getMessageType())
        {
            case CHAT_MESSAGE:
                broadcastExcept(message.getSenderId(), message);
                break;

        }
    }
}
