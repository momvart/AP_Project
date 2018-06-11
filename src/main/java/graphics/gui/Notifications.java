package graphics.gui;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Notifications
{
    public static void showQuitAlert(String text)
    {
        // TODO: 6/11/18 Change hard code . 
        Stage stage = new Stage(StageStyle.TRANSPARENT);
        Group group = new Group();
        Scene scene = new Scene(group);
        scene.setFill(Color.TRANSPARENT);
        ImageView imageView = new ImageView(new Image("assets/menu/banners/singlewood.png"));

        HBox buttonBox = new HBox();
        HBox questionBox = new HBox();
        Label yes = new Label("YES");
        yes.setFont(Font.font("Ani", 30));
        Label no = new Label("NO");
        no.setFont(Font.font("Ani", 30));
        handleMouseEvent(yes, true, stage);
        handleMouseEvent(no, false, stage);
        Label question = new Label(text);
        question.setFont(Font.font("Ani", 30));
        questionBox.getChildren().addAll(question);
        buttonBox.getChildren().addAll(yes, no);
        buttonBox.setSpacing(40);
        VBox box = new VBox(questionBox, buttonBox);
        box.setLayoutX(scene.getWidth() / 2);
        box.setLayoutY(scene.getHeight() / 5);
        group.getChildren().addAll(imageView, box);
        stage.setScene(scene);
        stage.show();
    }

    private static void handleMouseEvent(Label label, boolean yes, Stage stage)
    {
        label.addEventHandler(MouseEvent.ANY, mouseEvent ->
        {
            if (mouseEvent.getEventType().equals(MouseEvent.MOUSE_MOVED))
                label.setTextFill(Color.RED);
            else if (mouseEvent.getEventType().equals(MouseEvent.MOUSE_CLICKED))
            {
                if (yes)
                    MainMenu.quit();
                stage.close();
            }
            else
                label.setTextFill(Color.SADDLEBROWN);

        });
    }
}
