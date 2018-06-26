package graphics;

import graphics.drawers.BuildingDrawer;
import graphics.drawers.Drawer;
import graphics.drawers.drawables.ImageDrawable;
import graphics.layers.Layer;
import graphics.positioning.IsometricPositioningSystem;
import graphics.positioning.PositioningSystem;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import models.World;
import models.buildings.*;
import utils.Point;
import utils.RectF;
import utils.SizeF;

public class BuildingsTest extends Application
{
    public static void main(String[] args)
    {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception
    {
        GraphicsValues.setScale(0.45);
        World.initialize();
        Group group = new Group();
        double width = 1200;
        double height = 800;
        Canvas canvas = new Canvas(1200, 800);

        group.getChildren().add(canvas);

        GraphicHandler handler = new GraphicHandler(canvas.getGraphicsContext2D(), new RectF(0, 0, canvas.getWidth(), canvas.getHeight()));
        GameScene gameScene = new GameScene(new SizeF(canvas.getWidth(), canvas.getHeight()));
        canvas.setOnMouseClicked(handler::handleMouseClick);

        handler.updateCamera(new RectF(0, -900, canvas.getWidth(), canvas.getHeight()));

        PositioningSystem.sScale = 50;
        Layer lFloor = new Layer(0, new RectF(0, 0, width, height), IsometricPositioningSystem.getInstance());

        Image img1 = new Image(getClass().getClassLoader().getResourceAsStream("assets/floor/isometric1.png"));
        Image img2 = new Image(getClass().getClassLoader().getResourceAsStream("assets/floor/isometric2.png"));
        for (int i = 0; i < 30; i++)
            for (int j = 0; j < 30; j++)
            {
                ImageDrawable drawable = new ImageDrawable((i + j) % 2 == 0 ? img1 : img2, 61);
                drawable.setPivot(.5, .5);
                Drawer drawer = new Drawer(drawable);
                drawer.setPosition(i, j);
                drawer.setLayer(lFloor);
            }


        Layer layer = new Layer(2, new RectF(0, 0, width, height), IsometricPositioningSystem.getInstance());

        for (int i = 0; i <= 8; i++)
        {
            Building building = BuildingFactory.createBuildingByType(ElixirStorage.class, new Point(i, i), i);
            for (int j = 1; j <= i; j++)
                building.upgrade();
            Drawer drawer = new BuildingDrawer(building);
            drawer.setClickListener((sender, event) -> System.out.println("Salam" + building.getLevel()));
            drawer.setLayer(layer);
        }


        gameScene.addLayer(lFloor);
        gameScene.addLayer(layer);
        handler.setScene(gameScene);

        new GameLooper(handler).start();

        primaryStage.setScene(new Scene(group, canvas.getWidth(), canvas.getHeight()));
        primaryStage.show();

    }
}
