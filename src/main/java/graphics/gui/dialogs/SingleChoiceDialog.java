package graphics.gui.dialogs;

import graphics.Fonts;
import graphics.layers.Layer;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.stage.Modality;
import javafx.stage.StageStyle;
import views.dialogs.DialogResult;
import views.dialogs.DialogResultCode;

public class SingleChoiceDialog extends GraphicDialog
{

    ButtonType yes = ButtonType.YES;
    ButtonType no = ButtonType.NO;

    public SingleChoiceDialog(Layer layer, double width, double height, String message)
    {
        super(layer, width, height, message);
    }

    public SingleChoiceDialog(Layer layer, double width, double height, String message, ButtonType yesButton, ButtonType noButton)
    {
        super(layer, width, height, message);
        yes = yesButton;
        no = noButton;
    }

    @Override
    public DialogResult showDialog()
    {
        Alert dialog = new Alert(Alert.AlertType.CONFIRMATION, message, yes, no);
        applyCss(dialog);
        System.err.println(message);
        dialog.setContentText(message);
        DialogResult dialogResult;
        dialog.initModality(Modality.WINDOW_MODAL);
        dialog.showAndWait();
        ButtonType buttonData = dialog.getResult();
        if (buttonData.equals(no))
            dialogResult = new DialogResult(DialogResultCode.NO);
        else if (buttonData.equals(yes))
            dialogResult = new DialogResult(DialogResultCode.YES);
        else
            dialogResult = new DialogResult(DialogResultCode.NO);
        dialog.close();
        return dialogResult;
    }

    public static void applyCss(Dialog dialog)
    {
        String fontName = Fonts.getMedium().getName();
        System.err.println(fontName);
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
