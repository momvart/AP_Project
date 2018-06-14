import controllers.MainController;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;
import models.World;
import views.ConsoleView;

import java.util.Scanner;

public class Main extends Application
{
    public static void main(String[] args)
    {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception
    {
        new Thread(() ->
        {
            World.initialize();
            Scanner scanner = new Scanner(System.in);
            new MainController(new ConsoleView(scanner)).start();
        }).start();
        primaryStage.setScene(new Scene(new Group(), 100, 100));
        primaryStage.show();
    }
}
