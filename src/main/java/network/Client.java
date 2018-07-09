package network;

import com.google.gson.Gson;

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

    public void sendMessage(String message)
    {
        Gson gson = new Gson();
        String toJson = gson.toJson(new Message(message, clientName));
        try
        {
            outputStream.write(toJson.getBytes(), 0, toJson.length());
        }
        catch (IOException ignored) {}
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

    @Override
    public void run()
    {
        setUp();
        receiveMessage();
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

    private void showMessage(String message)
    {
        Gson gson = new Gson();
        Message fromJson = gson.fromJson(message, Message.class);
        System.out.println(fromJson.clientName + ":" + fromJson.message);
    }

    @Override
    public void messageReceived(String message)
    {
        showMessage(message);
    }
}
