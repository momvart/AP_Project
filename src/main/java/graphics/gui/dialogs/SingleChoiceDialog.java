package graphics.gui.dialogs;

import graphics.Layer;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
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
        dialog.setContentText(message);
        DialogResult dialogResult;
        dialog.showAndWait();
        ButtonBar.ButtonData buttonData = dialog.getResult().getButtonData();
        System.err.println(buttonData.toString());
        if (buttonData.equals(ButtonBar.ButtonData.NO))
            dialogResult = new DialogResult(DialogResultCode.NO);
        else if (buttonData.equals(ButtonBar.ButtonData.YES))
            dialogResult = new DialogResult(DialogResultCode.YES);
        else
            dialogResult = new DialogResult(DialogResultCode.NO);
        dialog.close();
        return dialogResult;
    }
}
