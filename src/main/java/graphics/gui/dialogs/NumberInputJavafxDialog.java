package graphics.gui.dialogs;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;

import java.util.Optional;

public class NumberInputJavafxDialog
{
    private String message;
    private String title;

    public NumberInputJavafxDialog(String message, String title)
    {
        this.message = message;
        this.title = title;
    }

    public int showAlert()
    {
        Alert dialog = new Alert(Alert.AlertType.CONFIRMATION, message, ButtonType.OK);
        dialog.setContentText(message);
        dialog.setTitle(title);
        TextField textField = new TextField();
        Label label = new Label(message);
        label.setTextFill(Color.WHITE);
        HBox hBox = new HBox(label, textField);
        hBox.setSpacing(10);
        SingleChoiceDialog.applyCss(dialog);
        textField.setPrefWidth(100);
        dialog.getDialogPane().setContent(hBox);
        Optional<ButtonType> buttonType = dialog.showAndWait();
        return Integer.parseInt(textField.getText());
    }
}
