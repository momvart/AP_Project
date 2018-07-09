package network;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.DataInputStream;
import java.io.IOException;

public class Receiver extends Thread
{
    private GameClient client;
    private IOnMessageReceivedListener receiver;

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
                byte[] buffer = new byte[client.getSocket().getReceiveBufferSize()];

                int read = inputStream.read(buffer);
                String s = new String(buffer, 0, read);
                System.err.println(s + " received");
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
                    System.err.println("Message is not valid: " + new String(buffer, 0, read));
                }
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
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
}
