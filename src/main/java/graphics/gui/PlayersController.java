package graphics.gui;

import javafx.scene.control.ListView;
import network.ClientInfo;

import java.util.List;

public class PlayersController
{
    public ListView listView;

    public void showPlayers(List<ClientInfo> players)
    {
        players.forEach(clientInfo ->
                listView.getItems().add(clientInfo.getName()));
        listView.refresh();
    }
}
