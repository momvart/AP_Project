package graphics.gui;

import graphics.gui.dialogs.NumberInputJavafxDialog;
import graphics.gui.dialogs.PortAndIPJavafxDialog;
import graphics.gui.dialogs.TextInputJavafxDialog;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
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
    public Button btnHost;
    public Button btnJoin;
    public Button btnDisconnect;
    public VBox tabChat;
    public VBox tabLeaderBoard;
    public VBox tabPlayers;
    public Label lStatus;

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
        TextInputJavafxDialog nameD = new TextInputJavafxDialog("Enter your name", "Name");
        String name = nameD.showAlert();
        NumberInputJavafxDialog dialog = new NumberInputJavafxDialog("Enter port number", "Host");
        int port = dialog.showAlert();
        GameHost gameHost = null;
        try
        {
            gameHost = new GameHost(port);
            gameHost.start();
            GameClientC gameClientC = new GameClientC(port, "localhost");
            gameClientC.getInfo().setName(name);
            gameClientC.setUp();
            World.sCurrentClient = gameClientC;
            setConnectionStatus(true);
            setLStatusText(true);
            tabChatController.initialize(World.sCurrentClient);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void btnJoin_Click(ActionEvent actionEvent)
    {
        TextInputJavafxDialog nameD = new TextInputJavafxDialog("Enter your name", "Name");
        String name = nameD.showAlert();
        PortAndIPJavafxDialog dialog = new PortAndIPJavafxDialog("Enter host's ip and port", "Join");
        Pair<String, Integer> portAndIp = dialog.showAlert();
        try
        {
            GameClientC clientC = new GameClientC(portAndIp.getValue(), portAndIp.getKey());
            clientC.getInfo().setName(name);
            clientC.setUp();
            World.sCurrentClient = clientC;
            setConnectionStatus(true);
            setLStatusText(false);
            tabChatController.initialize(World.sCurrentClient);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

    }

    public void btnDisconnect_Click(ActionEvent actionEvent)
    {
        World.sCurrentClient = null;
        setConnectionStatus(false);
    }

    private void setConnectionStatus(boolean connected)
    {
        if (connected)
        {
            tabChat.setDisable(false);
            tabLeaderBoard.setDisable(false);
            tabPlayers.setDisable(false);
            btnDisconnect.setDisable(false);
            btnHost.setDisable(true);
            btnJoin.setDisable(true);
        }
        else
        {
            tabChat.setDisable(true);
            tabLeaderBoard.setDisable(true);
            tabPlayers.setDisable(true);
            btnDisconnect.setDisable(true);
            btnHost.setDisable(false);
            btnJoin.setDisable(false);
            lStatus.setText("disconnected");
        }
    }

    private void setLStatusText(boolean host)
    {
        if (host)
            lStatus.setText(World.sCurrentClient.getInfo().getName() + " (host)");
        else
            lStatus.setText(World.sCurrentClient.getInfo().getName() + " (client)");
    }
}
