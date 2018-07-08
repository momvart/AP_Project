package network;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.function.Consumer;

public class Receiver implements Runnable
{
    private Client client;
    private Consumer<String> consumer;

    public Receiver(Client client)
    {
        this.client = client;
    }

    public void setConsumer(Consumer<String> consumer)
    {
        this.consumer = consumer;
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
                    consumer.accept(s);
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }

}
