package network;

import com.google.gson.Gson;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server implements Runnable
{
    private ServerSocket serverSocket;
    private ArrayList<DataInputStream> inputStreams = new ArrayList<>();
    private ArrayList<DataOutputStream> outputStreams = new ArrayList<>();
    private ArrayList<Client> clients = new ArrayList<>();
    private ArrayList<Receiver> receivers = new ArrayList<>();
    private int port;


    public Server(int port)
    {
        this.port = port;
    }

    @Override
    public void run()
    {
        try
        {
            setUp();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    private void setUp() throws IOException
    {
        serverSocket = new ServerSocket(port);
        while (true)
        {
            Socket accept = serverSocket.accept();
            InputStream inputStream = accept.getInputStream();
            DataInputStream dataInputStream = new DataInputStream(inputStream);
            OutputStream outputStream = accept.getOutputStream();
            DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
            inputStreams.add(dataInputStream);
            outputStreams.add(dataOutputStream);
            clients.add(new Client(dataInputStream, dataOutputStream));
            receiveMessage(dataInputStream, dataOutputStream);
        }
    }

    //clients message
    private void sendMessage(String message)
    {
        Gson gson = new Gson();
        Message fromJson = gson.fromJson(message, Message.class);
        outputStreams.forEach(outputStream -> {
            try
            {
                outputStream.write(message.getBytes(), 0, message.length());
                outputStream.flush();
            }
            catch (IOException ignored) {}
        });
    }

    //server message
    public void sendMessage(Message message)
    {
        Gson gson = new Gson();
        String toJson = gson.toJson(message);
        outputStreams.forEach(outputStream -> {
            try
            {
                outputStream.write(toJson.getBytes(), 0, toJson.length());
                outputStream.flush();
            }
            catch (IOException ignored) {}
        });
    }

    private void receiveMessage(DataInputStream inputStream, DataOutputStream outputStream)
    {
        Receiver receiver = new Receiver(new Client(inputStream, outputStream));
        receiver.setConsumer(this::sendMessage);
        new Thread(receiver).start();
    }
}
