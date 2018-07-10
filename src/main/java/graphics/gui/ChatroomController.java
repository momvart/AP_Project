package graphics.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import network.GameClientC;
import network.IOnChatMessageReceivedListener;

public class ChatroomController implements IOnChatMessageReceivedListener
{
    @FXML
    public ListView chatLists;


    @FXML
    public TextField txtMessage;


    private GameClientC client;

    public void initialize(GameClientC client)
    {
        this.client = client;
        client.setChatMessageReceiver(this);
    }

    public void btnSend_Click(ActionEvent event)
    {
        chatLists.getItems().add(txtMessage.getText());
        chatLists.refresh();
    }

    @Override
    public void onChatMessageReceived(String from, String message)
    {
        chatLists.getItems().add(message);

    }
}
