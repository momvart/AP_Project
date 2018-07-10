package graphics.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Tab;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import models.World;


public class NetworkTabPaneFXMLController
{
    @FXML
    public ChatroomController tabChatController;

    @FXML
    public LeaderBoardController tabLeaderBoardController;

    @FXML
    public PlayersController tabPlayersController;

    public void initialize()
    {
        if (World.sCurrentClient == null)
        {
            //TODO: disable tabs and make some connections
        }
        else
        {
            tabChatController.initialize(World.sCurrentClient);
        }
    }

    public void btnServer_Click(ActionEvent actionEvent)
    {

    }

    public void btnJoin_Click(ActionEvent actionEvent)
    {

    }
}
