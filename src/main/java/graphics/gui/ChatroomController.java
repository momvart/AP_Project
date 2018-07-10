package graphics.gui;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import network.Message;

public class ChatroomController
{
    public VBox vbox;
    public ListView chatLists;
    public HBox hbox;
    public TextField messageField;
    public Button sendBtn;


    public void handleSendButtonCLicked(ActionEvent event)
    {
        chatLists.getItems().add(messageField.getText());
        chatLists.refresh();
    }

    public void messageReceived(Message message)
    {
        chatLists.getItems().add(message.getMessage());
        chatLists.refresh();
    }
}
