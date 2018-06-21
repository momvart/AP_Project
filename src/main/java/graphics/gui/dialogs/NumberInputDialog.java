package graphics.gui.dialogs;

import graphics.Layer;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.Dialog;
import views.dialogs.DialogResult;
import views.dialogs.DialogResultCode;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


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
        Dialog dialog = new ChoiceDialog(1, choices);
        dialog.setContentText(message);
        Optional optional = dialog.showAndWait();
        if (optional.isPresent())
            return new DialogResult(DialogResultCode.YES).addData("number", (int)optional.get());
        dialog.close();
        return new DialogResult(DialogResultCode.NO);
    }
}
