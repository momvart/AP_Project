package network;

import com.google.gson.Gson;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Optional;

public class GameHost extends Thread implements IOnMessageReceivedListener
{
    private ServerSocket serverSocket;
    private ArrayList<DataInputStream> inputStreams = new ArrayList<>();
    private ArrayList<DataOutputStream> outputStreams = new ArrayList<>();
    private ArrayList<Client> clients = new ArrayList<>();
    private ArrayList<Receiver> receivers = new ArrayList<>();
    private int port;

    public GameHost(int port)
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
            Client client = new Client(dataInputStream, dataOutputStream, clients.size() + 1);
            clients.add(client);
            receiveMessage(client);
            sendMessage(new Message("new player joined", "Server", MessageType.SERVER_MESSAGE));
        }
    }

    //send client message to other clients
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

    //send message to all clients
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

    //send message to specific client
    public void sendMessage(Message message, int clientId)
    {
        Gson gson = new Gson();
        String toJson = gson.toJson(message);
        Optional<Client> cl = clients.stream().filter(client -> client.getClientId() == clientId).findFirst();
        Client client = cl.get();
        try
        {
            client.getOutputStream().write(toJson.getBytes(), 0, toJson.length());
        }
        catch (IOException ignored) {}
    }

    private void receiveMessage(Client client)
    {
        Receiver receiver = new Receiver(client);
        receiver.setListener(this);
        receiver.start();
    }

    @Override
    public void messageReceived(String message)
    {
        sendMessage(message);
    }
}
