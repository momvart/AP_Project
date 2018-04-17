import controllers.MainController;
import models.World;
import views.ConsoleView;

import java.util.Scanner;

public class Main
{
    public static void main(String[] args)
    {
        World.initialize();
        Scanner scanner = new Scanner(System.in);
        new MainController(new ConsoleView(scanner)).start();
    }
}
