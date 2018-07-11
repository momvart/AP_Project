package graphics.gui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class NetworkStage extends Stage
{
    public void start() throws Exception
    {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("layout/networkstage.fxml"));
        Scene scene = new Scene(new StackPane());
        scene.setRoot(fxmlLoader.load());
        setScene(scene);
        initModality(Modality.WINDOW_MODAL);
        show();
    }
}
