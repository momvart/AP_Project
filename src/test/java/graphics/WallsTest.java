package graphics;

import graphics.drawers.Drawer;
import graphics.drawers.drawables.ImageDrawable;
import graphics.positioning.IsometricPositioningSystem;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.stage.Stage;
import utils.GraphicsUtilities;
import utils.RectF;
import utils.SizeF;

import java.net.URISyntaxException;

public class WallsTest extends Application
{
    public static void main(String[] args)
    {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception
    {
        Group group = new Group();
        Canvas canvas = new Canvas(400, 400);
        group.getChildren().add(canvas);

        GraphicHandler handler = new GraphicHandler(canvas.getGraphicsContext2D(), new RectF(0, 0, 400, 400));
        canvas.setOnMouseClicked(handler::handleMouseClick);
        GameScene gameScene = new GameScene(new SizeF(400, 400));
        IsometricPositioningSystem.sScale = 20;
        Layer layer = new Layer(1, new RectF(0, 0, 400, 400), new IsometricPositioningSystem(20));
        Layer lFloor = new Layer(0, new RectF(0, 0, 400, 400), new IsometricPositioningSystem(20));
        {
            try
            {
                ImageDrawable tile1 = GraphicsUtilities.createImageDrawable("assets/floor/isometric1.png", IsometricPositioningSystem.sScale * IsometricPositioningSystem.ANG_COS * 2, IsometricPositioningSystem.sScale * IsometricPositioningSystem.ANG_SIN * 2, true);
                tile1.setPivot(.5, .5);
                ImageDrawable tile2 = GraphicsUtilities.createImageDrawable("assets/floor/isometric2.png", IsometricPositioningSystem.sScale * IsometricPositioningSystem.ANG_COS * 2, IsometricPositioningSystem.sScale * IsometricPositioningSystem.ANG_SIN * 2, true);
                tile2.setPivot(.5, .5);
                for (int i = 0; i < 30; i++)
                    for (int j = 0; j < 30; j++)
                    {
                        Drawer drawer = new Drawer((i + j) % 2 == 0 ? tile1 : tile2);
                        drawer.setPosition(i, j);
                        drawer.setLayer(lFloor);
                    }
            }
            catch (URISyntaxException e)
            {
                e.printStackTrace();
            }
        }

        ImageDrawable wall1 = GraphicsUtilities.createImageDrawable("assets/buildings/wall/upright/000/001.png", 32, 24, true);
        Drawer dwall1 = new Drawer(wall1);
        dwall1.setPosition(0, 5);
        dwall1.setLayer(layer);
        wall1.setScale(1.3, 1.3);

        ImageDrawable wall2 = GraphicsUtilities.createImageDrawable("assets/buildings/wall/right/000/001.png", 32, 24, true);
        Drawer dwall2 = new Drawer(wall2);
        dwall2.setPosition(0, 4);
        dwall2.setLayer(layer);
        wall2.setScale(1.3, 1.3);

        ImageDrawable wall3 = GraphicsUtilities.createImageDrawable("assets/buildings/wall/up/000/001.png", 32, 24, true);
        Drawer dwall3 = new Drawer(wall3);
        dwall3.setPosition(1, 5);
        dwall3.setLayer(layer);
        wall3.setScale(1.3, 1.3);

        ImageDrawable wall4 = GraphicsUtilities.createImageDrawable("assets/buildings/wall/static/000/001.png", 32, 24, true);
        Drawer dwall4 = new Drawer(wall4);
        dwall4.setPosition(1, 4);
        dwall4.setLayer(layer);
        wall4.setScale(1.3, 1.3);


        gameScene.addLayer(layer);
        gameScene.addLayer(lFloor);
        handler.setScene(gameScene);

        new GameLooper(handler).start();

        primaryStage.setScene(new Scene(group, 400, 400));
        primaryStage.show();
    }
}
