import models.World;
import network.*;

import java.io.IOException;
import java.util.Scanner;

public class NetworkTest
{
    public static void main(String[] args) throws IOException
    {
        World.initialize();
        World.newGame();

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
            client.setMessageReceiver(message -> System.out.println(message));
            client.setAttackMapReturnedListener((from, map) -> System.out.println("Map Received"));
            while (true)
            {
                String s1 = scanner.nextLine();
                if (s1.equals("map"))
                    client.sendMessage(new Message(scanner.nextLine(), client.getClientId(), MessageType.GET_MAP));
                else
                    client.sendChatMessage(s1);
            }
        }
    }
}
