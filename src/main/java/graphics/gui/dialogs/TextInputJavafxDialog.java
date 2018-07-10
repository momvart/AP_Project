package graphics.gui.dialogs;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;

public class TextInputJavafxDialog
{
    String message;
    String title;

    public TextInputJavafxDialog(String message, String title)
    {
        this.message = message;
        this.title = title;
    }

    public String showAlert()
    {
        Alert dialog = new Alert(Alert.AlertType.CONFIRMATION, message, ButtonType.OK);
        dialog.setContentText(message);
        dialog.setTitle(title);
        TextField textField = new TextField();
        Label label = new Label(message);
        label.setTextFill(Color.WHITE);
        HBox hBox = new HBox(label, textField);
        hBox.setSpacing(10);
        HBox.setHgrow(textField, Priority.ALWAYS);
        SingleChoiceDialog.applyCss(dialog);
        dialog.getDialogPane().setContent(hBox);
        dialog.showAndWait();
        return textField.getText();
    }
}
