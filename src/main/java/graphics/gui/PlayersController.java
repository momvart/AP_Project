package graphics.gui;

import graphics.Fonts;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.util.Pair;
import models.World;
import models.attack.Attack;
import models.attack.AttackMap;
import network.ClientInfo;
import network.GameClientC;
import network.Message;
import network.MessageType;

import java.util.List;
import java.util.UUID;

public class PlayersController
{
    private GameClientC client;

    @FXML
    public ListView<ClientInfo> listView;

    @FXML
    private ButtonBar btnBar;

    public void initialize(GameClientC client)
    {
        this.client = client;
        client.setOnPlayersListUpdatedListener(this::updatePlayersList);
        client.setAttackMapReturnedListener(this::onAttackMapReturned);
        listView.setCellFactory(list -> new PlayersListItem());
        listView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> btnBar.setDisable(newValue.getId().equals(client.getClientId())));
    }

    private void updatePlayersList()
    {
        Platform.runLater(() -> listView.getItems().setAll(World.sCurrentClient.getPlayersList()));
    }


    private UUID lastAttackReq;

    @FXML
    private void btnAttack_Click(ActionEvent event)
    {
        if (listView.getSelectionModel().getSelectedItems().isEmpty())
            return;
        lastAttackReq = listView.getSelectionModel().getSelectedItems().get(0).getId();
        client.sendMessage(new Message(lastAttackReq.toString(), client.getClientId(), MessageType.GET_MAP));
    }

    private void onAttackMapReturned(UUID from, AttackMap map)
    {
        if (!from.equals(lastAttackReq))
            return;

        Platform.runLater(() -> new AttackMapStage(new Attack(map, true), 1200, 900, true).setUpAndShow());
    }

    public static class PlayersListItem extends ListCell<ClientInfo>
    {
        @FXML
        private Label lblName;

        public PlayersListItem()
        {
            try
            {
                FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("layout/playerslistitem.fxml"));
                loader.setController(this);
                loader.load();

                lblName.setFont(Fonts.getBBMedium());
            }
            catch (Exception ex) {ex.printStackTrace();}
        }

        @Override
        protected void updateItem(ClientInfo item, boolean empty)
        {
            super.updateItem(item, empty);
            if (!empty)
            {
                lblName.setText(item.getName());

                if (item.getId().equals(World.sCurrentClient.getClientId()))
                    lblName.setTextFill(Color.web("#1815d9"));
                else
                    lblName.setTextFill(Color.BLACK);

                setGraphic(lblName.getParent());
                setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
            }
            else
            {
                setContentDisplay(ContentDisplay.TEXT_ONLY);
                setText("");
            }
        }
    }
}
