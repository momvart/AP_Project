package network;

import com.google.gson.Gson;

import java.io.*;
import java.net.Socket;
import java.util.UUID;
import java.util.function.Consumer;

public class GameClient implements IOnMessageReceivedListener
{
    protected ClientInfo info;

    private Socket socket;

    protected Receiver receiver;

    protected Gson deserializer = new Gson();

    private IOnMessageReceivedListener messageReceiver;

    private Consumer<GameClient> onConnectionClosedListener;


    public GameClient(Socket socket) throws IOException
    {
        this.socket = socket;
        writer = new DataOutputStream(socket.getOutputStream());
        this.info = new ClientInfo();
    }

    public Socket getSocket()
    {
        return socket;
    }

    public ClientInfo getInfo()
    {
        return info;
    }

    public void setInfo(ClientInfo info)
    {
        this.info = info;
    }

    public UUID getClientId()
    {
        return info.getId();
    }

    private DataOutputStream writer;

    private void callOnConnectionClosed()
    {
        if (onConnectionClosedListener != null)
            onConnectionClosedListener.accept(this);
    }

    public void setOnConnectionClosedListener(Consumer<GameClient> onConnectionClosedListener)
    {
        this.onConnectionClosedListener = onConnectionClosedListener;
    }

    public void sendMessage(Message message)
    {
        Gson gson = new Gson();
        byte[] json = (gson.toJson(message) + ";;").getBytes();
        try
        {
            writer.write(json, 0, json.length);
            writer.flush();
        }
        catch (IOException ignored) {}
    }

    public void setUp()
    {
        receiver = new Receiver(this);
        receiver.start();
        receiver.setOnConnectionClosedListener(this::callOnConnectionClosed);
    }

    public void close()
    {
        try
        {
            socket.close();
        }
        catch (IOException ex) { ex.printStackTrace(); }
    }

    private void callOnMessageReceived(Message message)
    {
        if (messageReceiver != null)
            messageReceiver.messageReceived(message);
    }

    public void setMessageReceiver(IOnMessageReceivedListener messageReceiver)
    {
        this.messageReceiver = messageReceiver;
    }

    @Override
    public void messageReceived(Message message)
    {
        callOnMessageReceived(message);
    }
}
