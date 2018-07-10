package graphics.gui.dialogs;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.util.Pair;

public class PortAndIPJavafxDialog
{
    private static final String PORT = "Port: ";
    private static final String IP = "IP: ";

    private String message;
    private String title;

    public PortAndIPJavafxDialog(String message, String title)
    {
        this.message = message;
        this.title = title;
    }

    public Pair<String, Integer> showAlert()
    {
        Alert dialog = new Alert(Alert.AlertType.CONFIRMATION, message, ButtonType.OK);
        dialog.setContentText(message);
        dialog.setTitle(title);
        TextField textField = new TextField();
        Label label = new Label(message);
        label.setTextFill(Color.WHITE);
        Label port = new Label(PORT);
        Label ip = new Label(IP);
        port.setTextFill(Color.WHITE);
        ip.setTextFill(Color.WHITE);
        TextField TFip = new TextField();
        TextField TFport = new TextField();

        GridPane gridPane = new GridPane();
        gridPane.add(label, 0, 0);
        gridPane.add(port, 0, 1);
        gridPane.add(TFport, 1, 1);
        gridPane.add(ip, 0, 2);
        gridPane.add(TFip, 1, 2);

        gridPane.setHgap(10);
        gridPane.setVgap(10);

        SingleChoiceDialog.applyCss(dialog);
        textField.setPrefWidth(100);
        dialog.getDialogPane().setContent(gridPane);
        dialog.showAndWait();
        return new Pair<>(TFip.getText(), Integer.parseInt(TFport.getText()));
    }
}
