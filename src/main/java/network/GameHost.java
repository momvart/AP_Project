package network;

import com.google.gson.Gson;
import exceptions.GameClientNotFoundException;
import models.attack.AttackReport;

import java.io.*;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class GameHost extends Thread implements IOnMessageReceivedListener
{
    private ServerSocket serverSocket;

    private HashMap<UUID, GameClient> clients = new HashMap<>();

    private ArrayList<AttackReport> attackReports = new ArrayList<>();

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
                client.setOnConnectionClosedListener(this::onClientConnectionClosed);
                broadcastClientsList();
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void close()
    {
        try
        {
            serverSocket.close();
            clients.values().forEach(GameClient::close);
        }
        catch (IOException ex) { ex.printStackTrace(); }
    }

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

    public void sendMessage(UUID clientId, Message message) throws GameClientNotFoundException
    {
        try
        {
            clients.get(clientId).sendMessage(message);
        }
        catch (NullPointerException ex) { throw new GameClientNotFoundException(clientId); }
    }

    @Override
    public void messageReceived(Message message)
    {
        switch (message.getMessageType())
        {
            case CHAT_MESSAGE:
                broadcastMessage(message);
                break;
            case GET_MAP:
                try
                {
                    sendMessage(UUID.fromString(message.getMessage()), message);
                }
                catch (GameClientNotFoundException | IllegalArgumentException ex)
                {
                    sendMessage(message.getSenderId(), new Message(ex.getMessage(), null, MessageType.ERROR));
                }
                break;
            case RET_MAP:
                try
                {
                    sendMessage(UUID.fromString(message.getMetadata()), message);
                }
                catch (GameClientNotFoundException | IllegalArgumentException ex)
                {
                    sendMessage(message.getSenderId(), new Message(ex.getMessage(), null, MessageType.ERROR));
                }
                break;
            case ATTACK_REPORT:
                AttackReport report = gson.fromJson(message.getMessage(), AttackReport.class);
                attackReports.add(report);
                callOnAttackReportReceived(report);
                break;
            case SET_CLIENT_INFO:
                clients.get(message.getSenderId()).setInfo(gson.fromJson(message.getMessage(), ClientInfo.class));
                broadcastClientsList();
                break;
        }
    }

    private Consumer<AttackReport> attackReportListener;

    private void callOnAttackReportReceived(AttackReport report)
    {
        if (attackReportListener != null)
            attackReportListener.accept(report);
    }

    public void setAttackReportListener(Consumer<AttackReport> attackReportListener)
    {
        this.attackReportListener = attackReportListener;
    }

    private void onClientConnectionClosed(GameClient client)
    {
        clients.remove(client.getClientId());
        broadcastClientsList();
    }
}
