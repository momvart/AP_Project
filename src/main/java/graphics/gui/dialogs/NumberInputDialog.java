package graphics.gui.dialogs;

import graphics.layers.Layer;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Spinner;
import views.dialogs.DialogResult;
import views.dialogs.DialogResultCode;

import java.util.ArrayList;
import java.util.List;


public class NumberInputDialog extends GraphicDialog
{
    private int size;

    public NumberInputDialog(Layer layer, double width, double height, String message, int size)
    {
        super(layer, width, height, message);
        this.size = size;
    }

    @Override
    public DialogResult showDialog()
    {
        List<Integer> choices = new ArrayList<>();
        for (int i = 0; i < size; i++)
        {
            choices.add(i + 1);
        }
        Spinner<Integer> spinner = new Spinner<>(1, choices.size(), 1, 1);
        spinner.setEditable(true);
        Alert dialog = new Alert(Alert.AlertType.CONFIRMATION, message, ButtonType.OK, ButtonType.CANCEL);
        dialog.setContentText(message);
        dialog.setGraphic(spinner);
        SingleChoiceDialog.applyCss(dialog);
        dialog.showAndWait();
        if (dialog.getResult().equals(ButtonType.OK))
            return new DialogResult(DialogResultCode.YES).addData("number", spinner.getValue());
        return new DialogResult(DialogResultCode.NO);
    }
}
