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

    private NetworkStageController controller;

    private NetworkStage()
    {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("layout/networkstage.fxml"));
        Scene scene = new Scene(new StackPane());
        try { scene.setRoot(fxmlLoader.load()); }
        catch (Exception ex) {}
        setScene(scene);
        initModality(Modality.WINDOW_MODAL);
        initStyle(StageStyle.UNDECORATED);
        this.controller = fxmlLoader.getController();
        show();
    }

    public NetworkStageController getController()
    {
        return controller;
    }

    public static NetworkStage getInstance()
    {
        if (instance == null)
            instance = new NetworkStage();
        return instance;
    }

    public static void showInstance()
    {
        getInstance().show();
    }


}
