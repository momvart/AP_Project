package network;

import com.google.gson.Gson;

import java.io.*;
import java.net.Socket;

public class Client implements Runnable
{
    private String name;
    private int port;
    private String ip;
    private Socket socket;
    private DataInputStream inputStream;
    private DataOutputStream outputStream;

    public Client(DataInputStream inputStream, DataOutputStream outputStream)
    {
        this.inputStream = inputStream;
        this.outputStream = outputStream;
    }

    public Client(String name, int port, String ip)
    {
        this.name = name;
        this.port = port;
        this.ip = ip;
    }

    public void sendMessage(String message)
    {
        Gson gson = new Gson();
        String toJson = gson.toJson(new Message(message, name));
        try
        {
            outputStream.write(toJson.getBytes(), 0, toJson.length());
        }
        catch (IOException ignored) {}
    }

    public String getName()
    {
        return name;
    }

    public DataInputStream getInputStream()
    {
        return inputStream;
    }

    public DataOutputStream getOutputStream()
    {
        return outputStream;
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
        receiver.setConsumer(this::showMessage);
        new Thread(receiver).start();
    }

    private void setUp()
    {
        try
        {
            socket = new Socket("localhost", 8888);
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
}
