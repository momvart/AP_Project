package network;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.SocketException;

public class Receiver extends Thread
{
    private GameClient client;

    private IOnMessageReceivedListener receiver;
    private Runnable onConnectionClosedListener;

    private Gson deserializer = new Gson();


    public Receiver(GameClient client)
    {
        this.client = client;
        this.receiver = client;
    }

    @Override
    public void run()
    {
        try (DataInputStream inputStream = new DataInputStream(client.getSocket().getInputStream()))
        {
            while (true)
            {
                byte[] buffer = new byte[20000];

                int read = inputStream.read(buffer);
                if (read == -1)
                    throw new SocketException("Connection closed.");
                String str = new String(buffer, 0, read);
                System.err.println("Received: " + str);
                try
                {
                    String[] datas = new String(buffer, 0, read).split(";;");
                    for (String strMessage : datas)
                    {
                        Message message = deserializer.fromJson(strMessage, Message.class);
                        callOnMessageReceived(message);
                    }
                }
                catch (JsonSyntaxException ex)
                {
                    System.err.println("Message is not valid: " + str);
                }
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
            callOnConnectionClosed();
        }
    }

    private void callOnMessageReceived(Message message)
    {
        if (receiver != null)
            receiver.messageReceived(message);
    }

    public void setReceiver(IOnMessageReceivedListener listener)
    {
        this.receiver = listener;
    }

    private void callOnConnectionClosed()
    {
        if (onConnectionClosedListener != null)
            onConnectionClosedListener.run();
    }

    public void setOnConnectionClosedListener(Runnable onConnectionClosedListener)
    {
        this.onConnectionClosedListener = onConnectionClosedListener;
    }
}
