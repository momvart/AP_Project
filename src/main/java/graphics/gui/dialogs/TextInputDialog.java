package graphics.gui.dialogs;

import graphics.layers.Layer;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
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
        HBox hBox = new HBox();
        hBox.setSpacing(10);
        hBox.setAlignment(Pos.CENTER);
        hBox.getChildren().addAll(new Label(message), textField);
        dialog.getDialogPane().setContent(hBox);
        SingleChoiceDialog.applyCss(dialog);
        dialog.initModality(Modality.WINDOW_MODAL);
        dialog.showAndWait();
        if (dialog.getResult().equals(ButtonType.OK))
            return new DialogResult(DialogResultCode.YES).addData("text", textField.getText());
        else
            return new DialogResult(DialogResultCode.NO);
    }
}
