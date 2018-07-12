package graphics.gui;

import graphics.Fonts;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import models.World;
import models.attack.Attack;
import models.attack.AttackMap;
import models.attack.AttackReport;
import network.ClientInfo;
import network.GameClientC;
import network.Message;
import network.MessageType;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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
        client.setPlayersListUpdatedListener(this::updatePlayersList);
        client.setAttackMapReturnedListener(this::onAttackMapReturned);
        client.setAttackStartListener(this::onAttackStarted);
        client.setAttackFinishListener(this::onAttackFinished);
        client.setDefenseStartListener(this::onDefenseStarted);
        client.setDefenseFinishListener(this::onDefenseFinished);
        listView.setCellFactory(list -> new PlayersListItem());
        listView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> btnBar.setDisable(newValue.getId().equals(client.getClientId())));
    }

    private void updatePlayersList()
    {
        Platform.runLater(() -> listView.getItems().setAll
                (World.sCurrentClient.getPlayersList().stream()
                        .sorted(Comparator.comparingInt(ClientInfo::getTotalTrophies).reversed())
                        .collect(Collectors.toList())));
    }


    @FXML
    private void btnRefresh_Click(ActionEvent event)
    {
        World.sCurrentClient.sendMessage("", MessageType.PLAYERS_LIST);
    }

    @FXML
    private void btnAttack_Click(ActionEvent event)
    {
        if (listView.getSelectionModel().getSelectedItems().isEmpty())
            return;
        client.sendMessage(new Message(listView.getSelectionModel().getSelectedItems().get(0).getId().toString(), client.getClientId(), MessageType.GET_MAP));
    }

    private HashMap<UUID, AttackMap> cachedMaps = new HashMap<>();

    private void onAttackMapReturned(UUID from, AttackMap map)
    {
        cachedMaps.put(from, map);

        Platform.runLater(() ->
        {
            AttackMapStage stage = new AttackMapStage(new Attack(map, true), 1200, 900, true);
            stage.setOnAttackStartRequestListener(() -> client.sendMessage(from.toString(), MessageType.ATTACK_REQUEST));
            stage.setupAndShow();
        });
    }

    private void onAttackStarted(UUID defenderId)
    {
        Platform.runLater(() ->
        {
            AttackStage stage = new AttackStage(new Attack(cachedMaps.get(defenderId), true), 1200, 900);
            stage.setAttackFinishedListener(report -> World.sCurrentClient.sendAttackReport(report));
            stage.setupAndShow();
        });
    }

    private void onAttackFinished(AttackReport report)
    {
        World.getVillage().addResource(report.getClaimedResource());
        List<Integer> spentTroops = report.getTroopsCount();
        for (int i = 0; i < spentTroops.size(); i++)
            if (spentTroops.get(i) > 0)
                World.getVillage().getSoldiers(i + 1).subList(0, spentTroops.get(i) - 1).clear();
    }

    private void onDefenseStarted(UUID attackerId)
    {
        VillageStage.getInstance().lockStageForAttack(client.getPlayerInfo(attackerId).getName());
    }

    private void onDefenseFinished(AttackReport report)
    {
        World.getVillage().spendResource(report.getClaimedResource(), true);
        //TODO: Maybe just storages amounts should be decreased.

        VillageStage.getInstance().unlockStageAfterAttack();
    }

    public static class PlayersListItem extends ListCell<ClientInfo>
    {
        @FXML
        private Label lblName;

        @FXML
        private Label lblTrophies;

        @FXML
        private ImageView imgTrophie;

        public PlayersListItem()
        {
            try
            {
                FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("layout/playeritem.fxml"));
                loader.setController(this);
                loader.load();

                lblName.setFont(Fonts.getBBMedium());
                lblTrophies.setFont(Fonts.getBBMedium());
                imgTrophie.fitHeightProperty().bind(lblTrophies.heightProperty());
                imgTrophie.fitWidthProperty().bind(imgTrophie.fitHeightProperty());
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
                lblTrophies.setText(Integer.toString(item.getTotalTrophies()));

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
