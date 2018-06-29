package graphics.gui.dialogs;

import graphics.layers.Layer;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import views.dialogs.DialogResult;
import views.dialogs.DialogResultCode;

public class TextInputDialog extends GraphicDialog
{
    public TextInputDialog(Layer layer, double width, double height, String message)
    {
        super(layer, width, height, message);
    }

    @Override
    public DialogResult showDialog()
    {
        Alert dialog = new Alert(Alert.AlertType.CONFIRMATION, message, ButtonType.OK, ButtonType.CANCEL);
        TextField textField = new TextField();
        dialog.setGraphic(textField);
        SingleChoiceDialog.applyCss(dialog);
        dialog.setContentText(message);
        dialog.initModality(Modality.WINDOW_MODAL);
        dialog.showAndWait();
        if (dialog.getResult().equals(ButtonType.OK))
            return new DialogResult(DialogResultCode.YES).addData("text", textField.getText());
        else
            return new DialogResult(DialogResultCode.NO);
    }
}
