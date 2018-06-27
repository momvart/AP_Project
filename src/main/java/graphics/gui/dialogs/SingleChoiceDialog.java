package graphics.gui.dialogs;

import graphics.Fonts;
import graphics.layers.Layer;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.StageStyle;
import views.dialogs.DialogResult;
import views.dialogs.DialogResultCode;

public class SingleChoiceDialog extends GraphicDialog
{

    public SingleChoiceDialog(Layer layer, double width, double height, String message)
    {
        super(layer, width, height, message);
    }

    @Override
    public DialogResult showDialog()
    {
        Alert dialog = new Alert(Alert.AlertType.CONFIRMATION, message, ButtonType.YES, ButtonType.NO);
        applyCss(dialog);
        System.err.println(message);
        dialog.setContentText(message);
        DialogResult dialogResult;
        dialog.initModality(Modality.WINDOW_MODAL);
        dialog.showAndWait();
        ButtonBar.ButtonData buttonData = dialog.getResult().getButtonData();
        if (buttonData.equals(ButtonBar.ButtonData.NO))
            dialogResult = new DialogResult(DialogResultCode.NO);
        else if (buttonData.equals(ButtonBar.ButtonData.YES))
            dialogResult = new DialogResult(DialogResultCode.YES);
        else
            dialogResult = new DialogResult(DialogResultCode.NO);
        dialog.close();
        return dialogResult;
    }

    public static void applyCss(Dialog dialog)
    {
        String fontName = Fonts.getMedium().getName();
        double fontSize = Fonts.getMedium().getSize();
        dialog.initStyle(StageStyle.TRANSPARENT);
        DialogPane dialogPane = dialog.getDialogPane();
        dialogPane.getStyleClass().remove("alert");
        dialogPane.setStyle("-fx-background-color: black;" +
                "-fx-alignment: center;" +
                "-fx-font-family: " + fontName + ";" + "-fx-font-size: " + Double.toString(fontSize) + ";" +
                "-fx-pref-width: 600;");
        dialogPane.lookup(".header-panel").setStyle("-fx-background-color: black;" +
                "-fx-text-alignment: center;");
        ;
        dialogPane.lookup(".header-panel .label").setStyle("-fx-wrap-text: true;" +
                "-fx-text-fill: lightgreen;");
        dialogPane.lookup(".content.label").setStyle("-fx-text-fill: white;");
    }
}
