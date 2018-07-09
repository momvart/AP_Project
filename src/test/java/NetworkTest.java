import network.GameClient;
import network.GameClientC;
import network.GameHost;
import network.Message;

import java.io.IOException;
import java.util.Scanner;

public class NetworkTest
{
    public static void main(String[] args) throws IOException
    {
        Scanner scanner = new Scanner(System.in);
        String s = scanner.nextLine();
        if (s.equals("server"))
        {
            GameHost gameHost = new GameHost(8888);
            gameHost.start();
        }
        else if (s.equals("client"))
        {
            GameClientC client = new GameClientC(8888, "localhost");
            client.setUp();
            client.setChatMessageReceiver(message -> System.out.println(message.getMessage()));
            while (true)
            {
                String s1 = scanner.nextLine();
                client.sendChatMessage(s1);
            }
        }
    }
}
