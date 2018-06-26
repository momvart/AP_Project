package graphics.gui;

import graphics.*;
import graphics.drawers.AnimationDrawer;
import graphics.drawers.Drawer;
import graphics.drawers.drawables.FrameAnimationDrawable;
import graphics.drawers.drawables.ImageDrawable;
import graphics.layers.Layer;
import graphics.positioning.PercentPositioningSystem;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import models.World;
import utils.GraphicsUtilities;
import utils.RectF;
import utils.SizeF;

import java.net.URISyntaxException;


public class MainMenu extends Application
{
    static Stage menu;

    @Override
    public void start(Stage stage) throws Exception
    {
        //region initiate

        GraphicsValues.setScale(1.5);

        final double width = 1920 / 2;
        final double height = 1080 / 2;
        if (System.getProperty("os.name").equals("Linux"))
            GraphicsValues.setScale(3);

        Group group = new Group();
        Canvas canvas = new Canvas(width * GraphicsValues.getScale(), height * GraphicsValues.getScale());
        group.getChildren().add(canvas);
        StackPane root = new StackPane();
        menu = stage;

        GraphicHandler handler = new GraphicHandler(canvas.getGraphicsContext2D(), new RectF(0, 0, width * GraphicsValues.getScale(), height * GraphicsValues.getScale()));
        GameScene gameScene = new GameScene(new SizeF(width, height));
        Layer layer = new Layer(0, new RectF(0, 0, width, height));
        root.setPrefWidth(GraphicsValues.getScale() * width);
        root.setPrefHeight(GraphicsValues.getScale() * height);
        canvas.setWidth(width * GraphicsValues.getScale());
        canvas.setHeight(height * GraphicsValues.getScale());
        canvas.setOnMouseClicked(handler::handleMouseClick);
        stage.setWidth(width * GraphicsValues.getScale());
        stage.setHeight(height * GraphicsValues.getScale());
        root.setAlignment(canvas, Pos.BOTTOM_LEFT);

        World.initialize();
        World.newGame();

        //endregion

        //region background
        ImageView imgBackground = new ImageView(new Image("assets/menu/background/night.png"));
        imgBackground.setFitWidth(root.getPrefWidth());
        imgBackground.setFitHeight(root.getPrefHeight());
        ImageDrawable background = new ImageDrawable(imgBackground.getImage(), width, height);
        Drawer bgdrawer = new Drawer(background);
        bgdrawer.setPosition(0, 0);
        bgdrawer.setLayer(layer);
        //endregion

        //region flame,stones
        Layer lFlame = new Layer(2, new RectF(0, 0, width, height));
        {
            lFlame.setPosSys(new PercentPositioningSystem(lFlame));

            ImageDrawable stones = new ImageDrawable(new Image("assets/menu/stones/1.png"), width / 10, width / 10);
            stones.setPivot(0.5, 0.5);
            Drawer stoneDrawer = new Drawer(stones);
            stoneDrawer.setPosition(0.45, 0.9);
            stoneDrawer.setLayer(lFlame);

            AnimationDrawer flameDrawer = new AnimationDrawer(new ImageDrawable(null));
            {
                FrameAnimationDrawable anim = GraphicsUtilities.createFrameAnimDrawableFrom(getClass().getClassLoader().getResource("assets/menu/flame").toURI(), 1, 100);
                anim.setPivot(0.5, 0.5);
                flameDrawer.addAnimation("flame", anim);
                flameDrawer.playAnimation("flame");
                flameDrawer.setPosition(.45, .9);
                flameDrawer.setLayer(lFlame);
            }

        }
        //endregion

        //region soldiers
        Layer soldierLayer = new Layer(1, new RectF(0, 0, width, height));
        soldierLayer.setPosSys(new PercentPositioningSystem(soldierLayer));
        idleSoldiers(soldierLayer, "guardian/4", 1.1f, 0.3f, 0.8f);
        idleSoldiers(soldierLayer, "wallBreaker/3", 1, 0.2f, 0.8f);
        //endregion

        //region menu buttons

        ImageDrawable woodenHanging = new ImageDrawable(new Image("assets/menu/woodenHanging/1.png"), width / 3, height * 0.8);
        Drawer woodenHangingDrawer = new Drawer(woodenHanging);
        woodenHanging.setPivot(0.5, 0);
        woodenHangingDrawer.setPosition(3 * width / 4, 0);
        woodenHangingDrawer.setLayer(layer);

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
            Notifications.showQuitAlert("Are you sure?");
        });
        initializeLabels(GraphicsValues.getScale(), width, height, newGame, loadGame, multiPlayer, setting, aboutUs, quit);
        handleMouseMovements(newGame, loadGame, multiPlayer, setting, aboutUs, quit);
        menuBox.getChildren().addAll(newGame, loadGame, multiPlayer, setting);
        quitBox.getChildren().addAll(aboutUs, quit);
        setUpBox(menuBox, (float)(0.20 * height), (float)height / 34, width, height, GraphicsValues.getScale());
        setUpBox(quitBox, (float)(0.61 * height), (float)height / 45, width, height, GraphicsValues.getScale());
        group.getChildren().addAll(menuBox, quitBox);
        //endregion

        Layer menu = new Layer(4, new RectF(0, 0, width, height));
        GraphicView gv = new GraphicView(menu, width, height);
        gv.showTopBar();
        gv.showLeftBar();
        gv.showRightBar();

        //region show
        gameScene.addLayer(layer);
        gameScene.addLayer(menu);
        gameScene.addLayer(gv.getLmenu());
        gameScene.addLayer(lFlame);
        gameScene.addLayer(soldierLayer);
        handler.setScene(gameScene);
        new GameLooper(handler).start();
        stage.setScene(new Scene(group, width * GraphicsValues.getScale(), height * GraphicsValues.getScale()));
        stage.setResizable(true);
        stage.show();
        //endregion
    }

    private void setUpBox(VBox menuBox, float layoutY, float spacing, double width, double height, double scale)
    {
        menuBox.setAlignment(Pos.CENTER);
        menuBox.setSpacing(spacing * scale);
        menuBox.setPrefWidth(width / 3 * scale);
        menuBox.setPrefHeight(0.21 * height * scale);
        menuBox.setLayoutX(3 * width / 4 * scale - menuBox.getPrefWidth() / 2);
        menuBox.setLayoutY(layoutY * scale);
    }

    private void initializeLabels(double scale, double width, double height, Label... labels)
    {
        for (Label label : labels)
        {
            label.setFont(Font.font("Ani", FontWeight.BOLD, 22.5 * scale));
            label.setTextFill(Color.SADDLEBROWN);
            label.prefWidth(width / 3 * scale);
            label.prefHeight(0.21 * height * scale);
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
                        ("assets/soldiers/" + folderNames + "/idle", 0.5, 100);
            }
            catch (URISyntaxException ignored) { }
            soldier.setScale(scale, scale);
            guardianAnimation.addAnimation("idle", soldier);
            guardianAnimation.playAnimation("idle");
            guardianAnimation.setPosition(x, y);
            guardianAnimation.setLayer(layer);
        }
    }

    public static void quit()
    {
        menu.close();
    }
}
