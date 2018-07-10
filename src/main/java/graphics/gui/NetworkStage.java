package graphics.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class NetworkStage extends Application
{
    @Override
    public void start(Stage stage) throws Exception
    {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/graphics/gui/FXMLs/NetworkTabPaneFXML.fxml"));
        Scene scene = new Scene(new StackPane());
        scene.setRoot(fxmlLoader.load());
        stage.setScene(scene);
        stage.show();
    }
}
