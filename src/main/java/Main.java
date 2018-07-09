import graphics.Fonts;
import graphics.gui.dialogs.SingleChoiceDialog;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import models.World;
import network.GameClient;
import network.GameClientC;
import network.GameHost;
import views.VillageView;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;
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
        //todo : change alerts to dialog
        newGame.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent ->
        {
            GameClient client = null;
            GameHost gameHost = null;
            ButtonType serverButton = new ButtonType("SERVER", ButtonBar.ButtonData.YES);
            ButtonType clientButton = new ButtonType("CLIENT", ButtonBar.ButtonData.YES);
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Which one?", serverButton, clientButton);
            SingleChoiceDialog.applyCss(alert);
            Optional<ButtonType> buttonType = alert.showAndWait();
            if (buttonType.get().getText().equals("SERVER"))
            {
                alert.setTitle("PORT");
                alert.setContentText("Insert port number");
                TextField textField = new TextField();
                alert.setGraphic(textField);
                alert.getButtonTypes().removeAll(serverButton, clientButton);
                alert.getButtonTypes().add(new ButtonType("OK", ButtonBar.ButtonData.YES));
                alert.showAndWait();
                try
                {
                    gameHost = new GameHost(Integer.parseInt(textField.getText()));
                    World.newGame();
                    new VillageView(new Scanner(System.in));
                    gameHost.start();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
            else
            {
                alert.setTitle("CLIENT");
                alert.setContentText("Insert ip and port");
                TextField ip = new TextField();
                TextField port = new TextField();
                Label ipL = new Label("IP:");
                ipL.setPrefWidth(ip.getPrefWidth());
                ipL.setTextFill(Color.YELLOWGREEN);
                Label portL = new Label("PORT:");
                portL.setPrefWidth(port.getPrefWidth());
                portL.setTextFill(Color.YELLOWGREEN);
                HBox hBox = new HBox(ipL, ip);
                HBox hBox1 = new HBox(portL, port);
                VBox vBox = new VBox(hBox, hBox1);
                vBox.setAlignment(Pos.CENTER_RIGHT);
                alert.setGraphic(vBox);
                alert.getButtonTypes().removeAll(serverButton, clientButton);
                alert.getButtonTypes().add(new ButtonType("OK", ButtonBar.ButtonData.YES));
                alert.showAndWait();
                try
                {
                    client = new GameClientC(Integer.parseInt(port.getText()), ip.getText());
                    World.newGame();
                    client.setUp();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
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
        settings.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent ->
        {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Set game speed", ButtonType.OK, ButtonType.CANCEL);
            Spinner<Double> spinner = new Spinner(1, 3, 1, 0.5);
            alert.setGraphic(spinner);
            SingleChoiceDialog.applyCss(alert);
            Optional<ButtonType> buttonType = alert.showAndWait();
            buttonType.ifPresent(buttonType1 ->
            {
                if (buttonType.get().equals(ButtonType.OK))
                {
                    World.sSettings.setGameSpeed(spinner.getValue());
                    alert.close();
                }
                else
                    alert.close();
            });
        });
        quit.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent ->
        {
            stage.close();
        });
    }
}
