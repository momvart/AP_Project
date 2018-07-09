package graphics;

import com.sun.prism.Graphics;
import graphics.drawers.Drawer;
import graphics.drawers.drawables.ChatMessageDrawable;
import graphics.drawers.drawables.HProgressbarDrawable;
import graphics.drawers.drawables.RoundRectDrawable;
import graphics.drawers.drawables.TextDrawable;
import graphics.layers.ChatLayer;
import graphics.layers.Layer;
import graphics.layers.MenuLayer;
import graphics.positioning.NormalPositioningSystem;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import menus.Menu;
import menus.ParentMenu;
import menus.Submenu;
import models.World;
import network.Message;
import utils.RectF;
import utils.SizeF;

public class BasicTextTest extends Application
{
    public static void main(String[] args)
    {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception
    {
        World.initialize();
        Group group = new Group();
        Canvas canvas = new Canvas(400, 400);
        group.getChildren().add(canvas);

        GraphicHandler handler = new GraphicHandler(canvas.getGraphicsContext2D(), new RectF(0, 0, 400, 400));
        canvas.setOnMouseClicked(handler::handleMouseClick);
        GameScene gameScene = new GameScene(new SizeF(400, 400));
        Layer layer = new Layer(0, new RectF(0, 0, 400, 400), new NormalPositioningSystem(10));

        Fonts.initialize();

        TextDrawable text = new TextDrawable("----", Color.BLACK, Font.font(20));
        text.setPivot(.5, .5);
        Drawer drawer = new Drawer(text);
        drawer.setPosition(0, 2);
        drawer.setLayer(layer);

        HProgressbarDrawable progressbar = new HProgressbarDrawable(new LinearGradient(0, 0, 0, 1, true, CycleMethod.NO_CYCLE, new Stop(0, Color.GOLD), new Stop(1, Color.ORANGE)));
        Drawer drawer2 = new Drawer(progressbar);
        progressbar.setSize(100, 10);
        progressbar.setProgress(.75);
        progressbar.setStroke(Color.BROWN);
        drawer2.setPosition(1, 10);
        drawer2.setLayer(layer);

        HProgressbarDrawable progressbar2 = new HProgressbarDrawable(new LinearGradient(0, 0, 0, 1, true, CycleMethod.NO_CYCLE, new Stop(0, Color.VIOLET), new Stop(1, Color.PURPLE)));
        Drawer drawer3 = new Drawer(progressbar2);
        progressbar2.setRightToLeft(true);
        progressbar2.setSize(100, 10);
        progressbar2.setBackground(Color.WHITE);
        progressbar2.setProgress(.20);
        progressbar2.setStroke(Color.BROWN);
        drawer3.setPosition(1, 15);
        drawer3.setLayer(layer);

        RoundRectDrawable tester = new RoundRectDrawable(400, 100, 10, Color.GREEN);
        Drawer drawer4 = new Drawer(tester);
        drawer4.setPosition(0, 0);
        drawer4.setLayer(layer);

        MenuLayer lMenu = new MenuLayer(2, new RectF(0, 0, 400, 100), MenuLayer.Orientation.VERTICAL);
        lMenu.setItemCellSize(50);
        ParentMenu mainMenu = new ParentMenu(Menu.Id.VILLAGE_MAIN_MENU, "");
        Submenu submenu = new Submenu(2, "Submenu", mainMenu);
        submenu.insertItem(3, "item");
        mainMenu.insertItem(submenu)
                .insertItem(Menu.Id.VILLAGE_RESOURCES, "resources")
                .insertItem(4, "item 2");
        lMenu.setCurrentMenu(mainMenu);
        lMenu.setClickListener(menu1 -> System.out.println(menu1.getText()));


        gameScene.addLayer(lMenu);
        gameScene.addLayer(layer);
        handler.setScene(gameScene);

        new GameLooper(handler).start();

        primaryStage.setScene(new Scene(group, 400, 400));
        primaryStage.show();
    }
}
