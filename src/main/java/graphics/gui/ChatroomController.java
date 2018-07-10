package graphics.gui;

import graphics.Fonts;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.util.Pair;
import network.GameClientC;
import network.IOnChatMessageReceivedListener;

public class ChatroomController implements IOnChatMessageReceivedListener
{
    @FXML
    public ListView<Pair<String, String>> chatLists;


    @FXML
    public TextField txtMessage;


    private GameClientC client;

    public void initialize(GameClientC client)
    {
        this.client = client;
        client.setChatMessageReceiver(this);

        chatLists.setCellFactory(list -> new ChatListItem());
    }

    public void btnSend_Click(ActionEvent event)
    {
        client.sendChatMessage(txtMessage.getText());
//        chatLists.getItems().add(new Pair<>("Mohammad: ", txtMessage.getText()));
//        chatLists.refresh();
    }

    @Override
    public void onChatMessageReceived(String from, String message)
    {
        chatLists.getItems().add(new Pair<>(from, message));
    }

    public static class ChatListItem extends ListCell<Pair<String, String>>
    {
        @FXML
        private Label lblTitle;

        @FXML
        private Label lblText;

        public ChatListItem()
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
        protected void updateItem(Pair<String, String> item, boolean empty)
        {
            super.updateItem(item, empty);
            if (!empty)
            {
                lblTitle.setText(item.getKey());
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
