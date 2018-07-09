package network;

import java.io.DataInputStream;
import java.io.IOException;

public class Receiver extends Thread
{
    private Client client;
    private IOnMessageReceivedListener listener;

    public Receiver(Client client)
    {
        this.client = client;
    }

    public void setListener(IOnMessageReceivedListener listener)
    {
        this.listener = listener;
    }

    @Override
    public void run()
    {
        if (client != null)
        {
            DataInputStream inputStream = client.getInputStream();
            while (true)
            {
                byte[] buffer = new byte[2000];
                try
                {
                    int read = inputStream.read(buffer);
                    String s = new String(buffer, 0, read);
                    System.err.println(s + " received");
                    callOnMessageReceive(s);
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }

    private void callOnMessageReceive(String message)
    {
        if (listener != null)
            listener.messageReceived(message);
    }
}
