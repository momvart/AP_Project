package graphics.gui;

import graphics.gui.dialogs.NumberInputJavafxDialog;
import graphics.gui.dialogs.PortAndIPJavafxDialog;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.util.Pair;
import models.World;
import network.GameClientC;
import network.GameHost;

import java.io.IOException;


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
        NumberInputJavafxDialog dialog = new NumberInputJavafxDialog("Enter port number", "Host");
        int port = dialog.showAlert();
        GameHost gameHost = null;
        try
        {
            gameHost = new GameHost(port);
            gameHost.start();
            GameClientC gameClientC = new GameClientC(port, "localhost");
            gameClientC.setUp();
            World.sCurrentClient = gameClientC;
            tabChatController.initialize(World.sCurrentClient);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void btnJoin_Click(ActionEvent actionEvent)
    {
        PortAndIPJavafxDialog dialog = new PortAndIPJavafxDialog("Enter host's ip and port", "Join");
        Pair<String, Integer> portAndIp = dialog.showAlert();
        try
        {
            GameClientC clientC = new GameClientC(portAndIp.getValue(), portAndIp.getKey());
            clientC.setUp();
            World.sCurrentClient = clientC;
            tabChatController.initialize(World.sCurrentClient);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

    }
}
