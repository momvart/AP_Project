package network;

import com.google.gson.Gson;
import graphics.gui.VillageStage;

import java.io.*;
import java.net.Socket;

public class Client extends Thread implements IOnMessageReceivedListener
{
    private int clientId;
    private String clientName;
    private int port;
    private String ip;
    private Socket socket;
    private DataInputStream inputStream;
    private DataOutputStream outputStream;
    private VillageStage villageStage;

    public Client(DataInputStream inputStream, DataOutputStream outputStream, int id)
    {
        this.clientId = id;
        this.inputStream = inputStream;
        this.outputStream = outputStream;
    }

    public Client(String clientName, int port, String ip)
    {
        this.clientName = clientName;
        this.port = port;
        this.ip = ip;
    }

    public String getClientName()
    {
        return clientName;
    }

    public DataInputStream getInputStream()
    {
        return inputStream;
    }

    public DataOutputStream getOutputStream()
    {
        return outputStream;
    }

    public int getClientId()
    {
        return clientId;
    }

    public void sendMessage(String message)
    {
        Gson gson = new Gson();
        String toJson = gson.toJson(new Message(message, clientName, MessageType.CHAT_MESSAGE));
        try
        {
            outputStream.write(toJson.getBytes(), 0, toJson.length());
        }
        catch (IOException ignored) {}
    }

    private void receiveMessage()
    {
        Receiver receiver = new Receiver(this);
        receiver.setListener(this);
        receiver.start();
    }

    private void setUp()
    {
        try
        {
            socket = new Socket(ip, port);
            InputStream inputStream = socket.getInputStream();
            this.inputStream = new DataInputStream(inputStream);
            OutputStream outputStream = socket.getOutputStream();
            this.outputStream = new DataOutputStream(outputStream);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

    }


    @Override
    public void run()
    {
        setUp();
        receiveMessage();
    }

    @Override
    public void messageReceived(String message)
    {
        Gson gson = new Gson();
        Message fromJson = gson.fromJson(message, Message.class);
        if (fromJson.messageType.equals(MessageType.CHAT_MESSAGE))
            villageStage.getChatLayer().newMessage(fromJson);
        else if (fromJson.messageType.equals(MessageType.ATTACK_REQUEST)) ;
        //attack request

    }
}
