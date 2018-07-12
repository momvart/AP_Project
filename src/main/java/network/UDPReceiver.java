package network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.function.Consumer;

public class UDPReceiver extends Thread
{
    private DatagramSocket socket;

    public UDPReceiver(int port)
    {
        this.socket = findFirstAvailablePort(port);
    }

    private Consumer<String> receiver;


    private static DatagramSocket findFirstAvailablePort(int port)
    {
        while (true)
            try
            {
                DatagramSocket socket = new DatagramSocket(port);
                return socket;
            }
            catch (SocketException ex)
            {
                port++;
            }
    }

    public DatagramSocket getSocket()
    {
        return socket;
    }

    @Override
    public void run()
    {
        while (true)
        {
            try
            {
                byte[] buffer = new byte[socket.getReceiveBufferSize()];
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);

                String str = new String(packet.getData(), 0, packet.getLength());
                callOnMessageReceived(str);
            }
            catch (IOException ex)
            {
                ex.printStackTrace();
                break;
            }
        }
    }

    protected void callOnMessageReceived(String message)
    {
        if (receiver != null)
            receiver.accept(message);
    }

    public void setReceiver(Consumer<String> receiver)
    {
        this.receiver = receiver;
    }
}
