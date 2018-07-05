package graphics.gui.dialogs;

import graphics.layers.Layer;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Spinner;
import javafx.stage.Modality;
import models.World;
import views.dialogs.DialogResult;
import views.dialogs.DialogResultCode;

import java.util.Optional;

public class SpinnerDialog extends GraphicDialog
{
    double initialValue;
    double bound;
    double step;

    public SpinnerDialog(Layer layer, double width, double height, String message, double initialValue, double bound, double step)
    {
        super(layer, width, height, message);
        this.initialValue = initialValue;
        this.bound = bound;
        this.step = step;
    }

    @Override
    public DialogResult showDialog()
    {
        Alert dialog = new Alert(Alert.AlertType.CONFIRMATION, message, ButtonType.OK, ButtonType.CANCEL);
        Spinner<Double> spinner = new Spinner<>(initialValue, bound, World.sSettings.getGameSpeed(), (float)step);
        spinner.setEditable(true);
        SingleChoiceDialog.applyCss(dialog);
        dialog.setGraphic(spinner);
        dialog.initModality(Modality.WINDOW_MODAL);
        Optional<ButtonType> buttonType = dialog.showAndWait();
        double v = spinner.getValue();
        if (buttonType.isPresent())
        {
            if (buttonType.get().equals(ButtonType.OK))
                return new DialogResult(DialogResultCode.YES).addData("speed", v);
            else
                return new DialogResult(DialogResultCode.NO);
        }
        return new DialogResult(DialogResultCode.NO);
    }
}
