package graphics.gui;

import graphics.GameLooper;
import graphics.GameScene;
import graphics.GraphicHandler;
import graphics.Layer;
import graphics.drawers.AnimationDrawer;
import graphics.drawers.Drawer;
import graphics.drawers.drawables.FrameAnimationDrawable;
import graphics.drawers.drawables.ImageDrawable;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import utils.GraphicsUtilities;
import utils.RectF;
import utils.SizeF;

import java.net.URISyntaxException;


public class MainMenu extends Application
{
    @Override
    public void start(Stage stage) throws Exception
    {
        //region initiate

        Group group = new Group();
        Canvas canvas = new Canvas(1920, 1080);
        group.getChildren().add(canvas);

        GraphicHandler handler = new GraphicHandler(canvas.getGraphicsContext2D(), new RectF(0, 0, 1920, 1080));
        GameScene gameScene = new GameScene(new SizeF(1920, 1080));
        Layer layer = new Layer(0, new RectF(0, 0, 1920, 1080));

        //endregion

        //region background
        ImageView imageView = new ImageView(new Image("assets/menu/background/night.png"));
        ImageDrawable imageDrawable = new ImageDrawable(imageView.getImage(), 1920, 1080);
        Drawer drawer = new Drawer(imageDrawable);
        drawer.setLayer(layer);
        //endregion

        //region flame,stones
        Layer layer1 = new Layer(1, new RectF(0, 0, 1920, 1080));
        AnimationDrawer flameDrawer = new AnimationDrawer(new ImageDrawable(null));
        {
            ImageDrawable[] imgs = new ImageDrawable[6];
            for (int i = 1; i <= 6; i++)
                imgs[i - 1] = new ImageDrawable(new Image(getClass().getClassLoader().getResourceAsStream("assets/menu/flame/" + Integer.toString(i) + ".png")), 400, 400);
            FrameAnimationDrawable animationDrawable = new FrameAnimationDrawable(imgs, 1);
            flameDrawer.addAnimation("flame", animationDrawable);
            flameDrawer.playAnimation("flame");
            flameDrawer.setPosition(1920 / 3 - 200, 720);
            flameDrawer.setLayer(layer1);
        }

        ImageDrawable stones = new ImageDrawable(new Image("assets/menu/stones/1.png"), 400, 400);
        Drawer stoneDrawer = new Drawer(stones);
        stoneDrawer.setPosition(1920 / 3 - 200, 670);
        stoneDrawer.setLayer(layer1);

        //endregion

        //region soldiers
        Layer soldierLayer = new Layer(2, new RectF(0, 0, 1920, 1080));
        idleSoldiers(soldierLayer, "guardian", 4, 220, 600);
        idleSoldiers(soldierLayer, "guardianLevel3", 4, 330, 700);

        //endregion

        //region menu buttons

        ImageDrawable woodenHanging = new ImageDrawable(new Image("assets/menu/woodenHanging/1.png"), 327 * 1.5, 568 * 1.5);
        Drawer woodenHangingDrawer = new Drawer(woodenHanging);
        woodenHangingDrawer.setPosition(3 * 1920 / 4 - 327 / 3, 0);
        woodenHangingDrawer.setLayer(layer1);

        VBox menuBox = new VBox();
        VBox quitBox = new VBox();
        Label newGame = new Label("NEW GAME");
        newGame.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent ->
        {
            System.err.println("NewGame clicked");
        });
        Label loadGame = new Label("LOAD GAME");
        loadGame.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent ->
        {
            System.err.println("Load game clicked");
        });
        Label multiPlayer = new Label("MULTI PLAYER");
        Label setting = new Label("SETTINGS");
        Label aboutUs = new Label("ABOUT US");
        Label quit = new Label("QUIT");
        quit.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent ->
        {
            stage.close();
        });
        initializeLabels(newGame, loadGame, multiPlayer, setting, aboutUs, quit);
        handleMouseMovements(newGame, loadGame, multiPlayer, setting, aboutUs, quit);
        menuBox.getChildren().addAll(newGame, loadGame, multiPlayer, setting);
        quitBox.getChildren().addAll(aboutUs, quit);
        setUpBox(menuBox, 200, 30);
        setUpBox(quitBox, 600, 15);
        group.getChildren().addAll(menuBox, quitBox);

        //endregion

        //region show
        gameScene.addLayer(layer);
        gameScene.addLayer(layer1);
        gameScene.addLayer(soldierLayer);
        handler.setScene(gameScene);
        new GameLooper(handler).start();
        stage.setScene(new Scene(group, 1920, 1080));
        stage.show();
        //endregion
    }

    private void setUpBox(VBox menuBox, int layoutY, int spacing)
    {
        menuBox.setSpacing(spacing);
        menuBox.setPrefWidth(327 * 1.5);
        menuBox.setPrefHeight(300);
        menuBox.setLayoutX(3 * 1920 / 4 - 327 / 3);
        menuBox.setLayoutY(layoutY);
        menuBox.setAlignment(Pos.CENTER);
    }

    private void initializeLabels(Label... labels)
    {
        for (Label label : labels)
        {
            label.setFont(Font.font("Ani", FontWeight.BOLD, 45));
            label.setTextFill(Color.SADDLEBROWN);
            label.prefWidth(327 * 1.5);
            label.prefHeight(100);
        }
    }

    private void handleMouseMovements(Label... labels)
    {
        for (Label label : labels)
        {
            label.addEventHandler(MouseEvent.ANY, mouseEvent ->
            {
                if (mouseEvent.getEventType().equals(MouseEvent.MOUSE_MOVED))
                    label.setTextFill(Color.RED);
                else
                    label.setTextFill(Color.SADDLEBROWN);
            });
        }
    }

    private void idleSoldiers(Layer layer, String folderNames, float scale, float x, float y)
    {
        AnimationDrawer guardianAnimation = new AnimationDrawer(new ImageDrawable(null));
        {
            FrameAnimationDrawable soldier = null;
            try
            {
                soldier = GraphicsUtilities.createFrameAnimDrawableFrom
                        (getClass().getClassLoader().getResource("assets/soldiers/" + folderNames + "/idle").toURI(), 0.5, 100);
            }
            catch (URISyntaxException ignored) { }
            soldier.setScale(scale, scale);
            guardianAnimation.addAnimation("idle", soldier);
            guardianAnimation.playAnimation("idle");
            guardianAnimation.setPosition(x, y);
            guardianAnimation.setLayer(layer);
        }
    }
}
