import controllers.MainController;
import graphics.Fonts;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import models.World;
import views.ConsoleView;
import views.VillageView;

import java.io.BufferedReader;
import java.nio.file.Files;
import java.nio.file.Paths;
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
//        new Thread(() ->
//        {
//            World.initialize();
//            Scanner scanner = new Scanner(System.in);
//            new MainController(new ConsoleView(scanner)).start();
//        }).start();
        World.initialize();
        Fonts.initialize();
        Group group = new Group();
        double width = 1200;
        double height = 900;
        ImageView imageView = setBackground();
        imageView.setEffect(new GaussianBlur(20));
        VBox vBox = setButtons(primaryStage, width, height);
        group.getChildren().addAll(imageView, vBox);
        primaryStage.setScene(new Scene(group, 1200, 900));
        primaryStage.show();
    }

    private VBox setButtons(Stage primaryStage, double width, double height)
    {
        Button newGame = new Button("NEW GAME");
        Button loadGame = new Button("LOAD GAME");
        Button setting = new Button("SETTINGS");
        Button quit = new Button("QUIT");
        applyCss(width, height, newGame, loadGame, setting, quit);
        handleColorEvents(newGame, loadGame, setting, quit);
        handleMouseClicks(primaryStage, newGame, loadGame, setting, quit);
        VBox vBox = new VBox(newGame, loadGame, setting, quit);
        vBox.setPrefWidth(width / 3);
        vBox.setLayoutX(width / 2 - vBox.getPrefWidth() / 2);
        vBox.setLayoutY(height / 6);
        vBox.setSpacing(height / 50);
        return vBox;
    }

    private ImageView setBackground()
    {
        ImageView imageView = new ImageView(new Image(getClass().getClassLoader().getResourceAsStream("assets/floor/background2.png")));
        imageView.setCache(true);
        imageView.prefWidth(1200);
        imageView.prefHeight(900);
        imageView.setFitWidth(1200);
        imageView.setFitHeight(900);
        return imageView;
    }

    private void applyCss(double width, double height, Button... buttons)
    {
        for (Button button : buttons)
        {
            button.setStyle("-fx-background-color: rgba(0,0,0,0.6);" +
                    "-fx-text-alignment: center;" +
                    "-fx-font-family: " + Fonts.getMedium().getName() + ";" +
                    "-fx-font-size: " + Fonts.getLarge().getSize() + ";" +
                    "-fx-pref-width: " + width / 3 + ";" +
                    "-fx-pref-height: " + height / 6 + ";" +
                    "-fx-text-fill: white;" +
                    "-fx-wrap-text: true");
        }
    }

    private void handleColorEvents(Button... buttons)
    {
        for (Button button : buttons)
        {
            button.addEventHandler(MouseEvent.ANY, mouseEvent ->
            {
                if (mouseEvent.getEventType().equals(MouseEvent.MOUSE_MOVED))
                    button.setTextFill(Color.YELLOWGREEN);
                else
                    button.setTextFill(Color.WHITE);
            });
        }
    }

    private void handleMouseClicks(Stage stage, Button newGame, Button loadGame, Button settings, Button quit)
    {
        newGame.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent ->
        {
            World.newGame();
            new VillageView(new Scanner(System.in));
            stage.close();
        });
        loadGame.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent ->
        {
            try (BufferedReader reader = Files.newBufferedReader(Paths.get("data", "save.json")))
            {
                World.openGame(reader);
                new VillageView(new Scanner(System.in));
                stage.close();
            }
            catch (Exception ignored)
            {
                ignored.printStackTrace();
            }
        });
        quit.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent ->
        {
            stage.close();
        });
    }
}
