package graphics.gui;

import graphics.Fonts;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Region;
import models.World;
import models.attack.AttackReport;
import network.MessageType;

import java.util.ArrayList;

public class AttackReportsController
{
    @FXML
    private ListView<AttackReport> listView;

    public void initialize()
    {
        if (World.sCurrentClient != null)
            World.sCurrentClient.setAttackReportsReceivedListener(this::onAttackReportsReceived);
        listView.setCellFactory(AttackReportListItem::new);
    }

    private void onAttackReportsReceived(ArrayList<AttackReport> reports)
    {
        Platform.runLater(() ->
        {
            listView.getItems().setAll(reports);
            listView.refresh();
        });
    }

    @FXML
    private void btnRefresh_Click(ActionEvent event)
    {
        World.sCurrentClient.sendMessage("", MessageType.ATTACK_REPORTS_LIST);
    }

    public static class AttackReportListItem extends ListCell<AttackReport>
    {
        @FXML
        private Label lblAttackerName;
        @FXML
        private Label lblDefenderName;

        @FXML
        private Label lblClaimedGold;
        @FXML
        private Label lblClaimedElixir;
        @FXML
        private Label lblClaimedTrophies;

        @FXML
        private ImageView imgGold;
        @FXML
        private ImageView imgElixir;
        @FXML
        private ImageView imgTrophie;

        public AttackReportListItem(ListView list)
        {
            try
            {
                FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("layout/attackreportitem.fxml"));
                loader.setController(this);
                loader.load();

                lblAttackerName.setFont(Fonts.getBBSmall());
                lblDefenderName.setFont(Fonts.getBBSmall());
                lblClaimedGold.setFont(Fonts.getBBSmall());
                lblClaimedElixir.setFont(Fonts.getBBSmall());
                lblClaimedTrophies.setFont(Fonts.getBBSmall());


                imgGold.fitHeightProperty().bind(lblClaimedGold.heightProperty());
                imgGold.fitWidthProperty().bind(imgGold.fitHeightProperty());
                imgElixir.fitHeightProperty().bind(lblClaimedGold.heightProperty());
                imgElixir.fitWidthProperty().bind(imgElixir.fitHeightProperty());
                imgTrophie.fitHeightProperty().bind(lblClaimedGold.heightProperty());
                imgTrophie.fitWidthProperty().bind(imgTrophie.fitHeightProperty());

                ((Region)lblClaimedGold.getParent()).prefHeightProperty().bind(lblClaimedGold.heightProperty());
            }
            catch (Exception ex) {ex.printStackTrace();}
        }

        @Override
        protected void updateItem(AttackReport item, boolean empty)
        {
            super.updateItem(item, empty);
            if (!empty)
            {
                lblAttackerName.setText(World.sCurrentClient.getPlayerInfo(item.getAttackerId()).getName());
                lblDefenderName.setText(World.sCurrentClient.getPlayerInfo(item.getDefenderId()).getName());
                lblClaimedGold.setText(Integer.toString(item.getClaimedResource().getGold()));
                lblClaimedElixir.setText(Integer.toString(item.getClaimedResource().getElixir()));
                lblClaimedTrophies.setText(Integer.toString(item.getClaimedScore()));

                setGraphic(lblClaimedGold.getParent().getParent());
                setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
            }
            else
            {
                setContentDisplay(ContentDisplay.TEXT_ONLY);
                setText("");
            }
        }

        @Override
        protected void updateBounds()
        {
            lblClaimedGold.getParent().requestLayout();
            lblClaimedGold.getParent().getParent().requestLayout();
            super.updateBounds();
        }
    }

}
