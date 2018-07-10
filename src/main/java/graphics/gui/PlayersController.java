package graphics.gui;

import graphics.Fonts;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Pair;
import models.World;
import network.ClientInfo;
import network.GameClientC;

import java.util.List;

public class PlayersController
{
    private GameClientC client;

    @FXML
    public ListView<ClientInfo> listView;

    public void initialize(GameClientC client)
    {
        this.client = client;
        client.setOnPlayersListUpdatedListener(this::updatePlayersList);
        listView.setCellFactory(list -> new PlayersListItem());
    }

    private void updatePlayersList()
    {
        Platform.runLater(() -> listView.getItems().setAll(World.sCurrentClient.getPlayersList()));
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
