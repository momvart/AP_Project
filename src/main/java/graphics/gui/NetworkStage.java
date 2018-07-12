package graphics.gui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class NetworkStage extends Stage
{
    private static NetworkStage instance;

    private NetworkStage()
    {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("layout/networkstage.fxml"));
        Scene scene = new Scene(new StackPane());
        try { scene.setRoot(fxmlLoader.load()); }
        catch (Exception ex) {}
        setScene(scene);
        initModality(Modality.WINDOW_MODAL);
        initStyle(StageStyle.UNDECORATED);
        show();
    }

    public static void showInstance()
    {
        if (instance == null)
            instance = new NetworkStage();
        instance.show();
    }


}
