package graphics.gui;

import graphics.Fonts;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.util.Pair;
import models.World;
import network.GameClientC;
import network.IOnChatMessageReceivedListener;

import java.util.UUID;

public class ChatroomController implements IOnChatMessageReceivedListener
{
    @FXML
    public ListView<Pair<UUID, String>> chatLists;


    @FXML
    public TextField txtMessage;


    private GameClientC client;

    public void initialize(GameClientC client)
    {
        this.client = client;
        client.setChatMessageReceiver(this);

        chatLists.setCellFactory(ChatListItem::new);
    }

    public void btnSend_Click(ActionEvent event)
    {
        client.sendChatMessage(txtMessage.getText());
        txtMessage.clear();
    }

    @Override
    public void onChatMessageReceived(UUID from, String message)
    {
        Platform.runLater(() -> chatLists.getItems().add(new Pair<>(from, message)));
    }

    public static class ChatListItem extends ListCell<Pair<UUID, String>>
    {
        @FXML
        private Label lblTitle;

        @FXML
        private Label lblText;


        public ChatListItem(ListView list)
        {
            try
            {
                FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("layout/chatlistitem.fxml"));
                loader.setController(this);
                loader.load();

                lblTitle.setFont(Fonts.getBBSmaller());
                lblText.setFont(Fonts.getBBSmall());
            }
            catch (Exception ex) {ex.printStackTrace();}
        }

        @Override
        protected void updateItem(Pair<UUID, String> item, boolean empty)
        {
            super.updateItem(item, empty);
            if (!empty)
            {
                if (World.sCurrentClient.getClientId().equals(item.getKey()))
                {
                    lblTitle.setText(":You");
                    lblTitle.setAlignment(Pos.CENTER_RIGHT);
                    lblText.setAlignment(Pos.CENTER_RIGHT);
                    lblTitle.setTextFill(Color.GOLD);
                    lblText.setTextFill(Color.WHITE);
                }
                else
                {
                    lblTitle.setText(World.sCurrentClient.getPlayerInfo(item.getKey()).getName() + ":");
                    lblTitle.setAlignment(Pos.CENTER_LEFT);
                    lblText.setAlignment(Pos.CENTER_LEFT);
                    lblTitle.setTextFill(Color.YELLOWGREEN);
                    lblText.setTextFill(Color.WHITE);
                }
                lblText.setText(item.getValue());
                setGraphic(lblText.getParent());
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
