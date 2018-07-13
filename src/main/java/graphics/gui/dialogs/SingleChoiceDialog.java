package graphics.gui.dialogs;

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
        dialog.initStyle(StageStyle.TRANSPARENT);
        DialogPane dialogPane = dialog.getDialogPane();
        dialogPane.getStyleClass().remove("alert");
        dialogPane.getStyleClass().remove("header-panel");
        dialogPane.getStylesheets().add("layout/dialogs.css");
        dialogPane.getStyleClass().add("dialog-pane-game");
    }
}
