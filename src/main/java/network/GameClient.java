package network;

import com.google.gson.Gson;

import java.io.*;
import java.net.Socket;
import java.util.UUID;

public class GameClient implements IOnMessageReceivedListener
{
    protected ClientInfo info;

    private Socket socket;

    protected Gson deserializer = new Gson();

    private IOnMessageReceivedListener messageReceiver;

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

    public UUID getClientId()
    {
        return info.getId();
    }

    private DataOutputStream writer;

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
        Receiver receiver = new Receiver(this);
        receiver.start();
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
