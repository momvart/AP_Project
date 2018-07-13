package graphics.gui.dialogs;

import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import models.World;

import java.util.Optional;

public class SettingsDialog
{
    private static final String TITLE = "SETTINGS";
    private static final String GAME_SPEED_MESSAGE = "Game speed: ";
    private static final String SOUND_STATUS_MESSAGE = "Sounds : ";
    private static final String PAUSED_BUTTON_TEXT = "Pause";
    private static final String SETTING_BUTTON_TEXT = "Settings";
    private static final String BACK_TO_MENU_BUTTON_TEXT = "Back to menu";
    private static final String BACK_TO_GAME_BUTTON_TEXT = "Back to game";

    private double gameSpeed;
    private boolean hasSounds;
    private boolean paused;
    private boolean backToMenu;

    public void showMenu()
    {
        Alert dialog = new Alert(Alert.AlertType.CONFIRMATION, "");
        VBox vBox = new VBox();
        Button btnPause = getButton(PAUSED_BUTTON_TEXT);
        Button btnSetting = getButton(SETTING_BUTTON_TEXT);
        Button btnMenu = getButton(BACK_TO_MENU_BUTTON_TEXT);
        Button btnBack = getButton(BACK_TO_GAME_BUTTON_TEXT);
        vBox.getChildren().addAll(btnPause, btnSetting, btnMenu, btnBack);
        vBox.setSpacing(10);
        vBox.setAlignment(Pos.CENTER);
        SingleChoiceDialog.applyCss(dialog);
        dialog.getDialogPane().setContent(vBox);
        dialog.getButtonTypes().removeAll(ButtonType.OK, ButtonType.CANCEL);
        btnPause.addEventHandler(MouseEvent.MOUSE_CLICKED, event ->
        {
            paused = !paused;
            dialog.close();
            Stage stage = (Stage)dialog.getDialogPane().getScene().getWindow();
            stage.close();
        });
        btnSetting.addEventHandler(MouseEvent.MOUSE_CLICKED, event ->
        {
            showSettingDialog();
            dialog.close();
            Stage stage = (Stage)dialog.getDialogPane().getScene().getWindow();
            stage.close();
        });
        btnMenu.addEventHandler(MouseEvent.MOUSE_CLICKED, event ->
        {
            backToMenu = true;
            dialog.close();
            Stage stage = (Stage)dialog.getDialogPane().getScene().getWindow();
            stage.close();
        });
        btnBack.addEventHandler(MouseEvent.MOUSE_CLICKED, event ->
        {
            dialog.close();
            Stage stage = (Stage)dialog.getDialogPane().getScene().getWindow();
            stage.close();
        });

        dialog.showAndWait();
    }

    public void showSettingDialog()
    {
        Alert dialog = new Alert(Alert.AlertType.CONFIRMATION, "", ButtonType.OK, ButtonType.CANCEL);
        dialog.setTitle(TITLE);
        SingleChoiceDialog.applyCss(dialog);
        GridPane gridPane = new GridPane();
        Spinner<Double> speedSpinner = getSpinner(1, 3, 0.5);
        CheckBox cbSound = getCheckBox();
        Label lblSound = getLabel(SOUND_STATUS_MESSAGE);
        Label lblSpeed = getLabel(GAME_SPEED_MESSAGE);

        gridPane.addRow(0, lblSpeed, speedSpinner);
        gridPane.addRow(1, lblSound, cbSound);

        gridPane.setVgap(10);
        gridPane.setHgap(10);
        gridPane.setAlignment(Pos.CENTER);
        dialog.getDialogPane().setContent(gridPane);

        Optional<ButtonType> buttonType = dialog.showAndWait();

        if (buttonType.get().equals(ButtonType.OK))
        {
            gameSpeed = speedSpinner.getValue();
            hasSounds = cbSound.isSelected();
        }
    }

    public double getGameSpeed()
    {
        return gameSpeed;
    }

    public boolean hasSounds()
    {
        return hasSounds;
    }

    public boolean isPaused()
    {
        return paused;
    }

    public boolean isBackToMenu()
    {
        return backToMenu;
    }

    private Spinner<Double> getSpinner(double initialValue, double bound, double step)
    {
        Spinner<Double> spinner = new Spinner<>(initialValue, bound, World.sSettings.getGameSpeed(), (float)step);
        spinner.setEditable(true);
        return spinner;
    }

    private CheckBox getCheckBox()
    {
        return new CheckBox();
    }

    private Label getLabel(String text)
    {
        Label label = new Label(text);
        label.setTextFill(Color.WHITE);
        return label;
    }

    private Button getButton(String text)
    {
        Button button = new Button(text);
        button.getStyleClass().add("button-settings");
        button.setOnMouseMoved(event -> button.setTextFill(Color.YELLOWGREEN));
        return button;
    }
}
