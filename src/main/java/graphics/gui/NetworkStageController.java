package graphics.gui;

import graphics.gui.dialogs.NumberInputJavafxDialog;
import graphics.gui.dialogs.PortAndIPJavafxDialog;
import graphics.gui.dialogs.TextInputJavafxDialog;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;
import javafx.util.Pair;
import models.World;
import network.GameClient;
import network.GameClientC;
import network.GameHost;

import java.io.IOException;


public class NetworkStageController
{
    @FXML
    public ChatroomController tabChatController;

    @FXML
    public LeaderBoardController tabLeaderBoardController;

    @FXML
    public PlayersController tabPlayersController;

    @FXML
    private AttackReportsController tabAttackReportsController;

    public Button btnHost;
    public Button btnJoin;

    public Button btnDisconnect;
    public Label lblStatus;

    @FXML
    public TabPane tabs;

    @FXML
    public AttackReportsController tabAttacksController;
    public Button btnBack;

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
        NumberInputJavafxDialog dialog = new NumberInputJavafxDialog("Enter port number:", "Host");
        int port = dialog.showAlert();
        try
        {
            GameHost gameHost = new GameHost(port);
            gameHost.start();
            World.sCurrentHost = gameHost;
            setupClient(port, "");

            TextInputJavafxDialog nameD = new TextInputJavafxDialog("Enter your name:", "Name");
            String name = nameD.showAlert();
            World.sCurrentClient.getInfo().setName(name);
            World.sCurrentClient.sendInfo();

            setConnectionStatus(true);
            setLStatusText(true);
        }
        catch (IOException e)
        {
            World.sCurrentClient = null;
            World.sCurrentHost = null;
        }
    }

    public void btnJoin_Click(ActionEvent actionEvent)
    {
        PortAndIPJavafxDialog dialog = new PortAndIPJavafxDialog("Enter host's ip and port:", "Join");
        Pair<String, Integer> portAndIp = dialog.showAlert();
        try
        {
            setupClient(portAndIp.getValue(), portAndIp.getKey());

            TextInputJavafxDialog nameD = new TextInputJavafxDialog("Enter your name:", "Name");
            String name = nameD.showAlert();
            World.sCurrentClient.getInfo().setName(name);
            World.sCurrentClient.sendInfo();

            setConnectionStatus(true);
            setLStatusText(false);
        }
        catch (IOException e)
        {
            e.printStackTrace();
            World.sCurrentClient = null;
        }

    }

    private void setupClient(int port, String ip) throws IOException
    {
        GameClientC gameClientC = new GameClientC(port, ip);
        gameClientC.setUp();
        gameClientC.setOnConnectionClosedListener(this::onConnectionClosed);
        World.sCurrentClient = gameClientC;

        tabChatController.initialize(World.sCurrentClient);
        tabPlayersController.initialize(World.sCurrentClient);
        tabAttackReportsController.initialize();
    }

    public void btnDisconnect_Click(ActionEvent actionEvent)
    {
        if (World.sCurrentHost != null)
        {
            World.sCurrentHost.close();
            World.sCurrentHost = null;
        }
        World.sCurrentClient.close();
        World.sCurrentClient = null;
        setConnectionStatus(false);
    }

    private void onConnectionClosed(GameClient client)
    {
        World.sCurrentHost = null;
        World.sCurrentClient = null;
        setConnectionStatus(false);
    }

    private void setConnectionStatus(boolean connected)
    {
        if (connected)
        {
            tabs.setDisable(false);
            btnDisconnect.setDisable(false);
            btnHost.getParent().setVisible(false);
            lblStatus.getParent().setVisible(true);
        }
        else
        {
            tabs.setDisable(true);
            btnDisconnect.setDisable(true);
            btnHost.getParent().setVisible(true);
            lblStatus.getParent().setVisible(false);
        }
    }

    private void setLStatusText(boolean host)
    {
        if (host)
            lblStatus.setText(World.sCurrentClient.getInfo().getName() + " (host)");
        else
            lblStatus.setText(World.sCurrentClient.getInfo().getName() + " (client)");
    }


    public void btnBack_Click(ActionEvent actionEvent)
    {
        Stage stage = (Stage)btnBack.getScene().getWindow();
        stage.close();
    }
}
