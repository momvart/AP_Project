package graphics.gui;

import graphics.*;
import graphics.drawers.BuildingDrawer;
import graphics.drawers.Drawer;
import graphics.drawers.drawables.ImageDrawable;
import graphics.positioning.IsometricPositioningSystem;
import graphics.positioning.PositioningSystem;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import models.Map;
import utils.RectF;
import utils.SizeF;

import java.net.URISyntaxException;

public class MapStage extends Stage
{
    private Map map;

    private GameLooper looper;
    private GraphicHandler handler;

    private GameScene gscene;
    private Layer lFloor;
    private Layer lObjects;

    public MapStage(Map map)
    {
        this.map = map;
    }

    public void setUpAndShow()
    {
        Group group = new Group();

        GraphicsValues.setScale(0.45);

        final double width = 1100;
        final double height = 800;
        Canvas canvas = new Canvas(width, height);
        group.getChildren().add(canvas);

        handler = new GraphicHandler(canvas.getGraphicsContext2D(), new RectF(0, 0, canvas.getWidth(), canvas.getHeight()));
        gscene = new GameScene(new SizeF(canvas.getWidth(), canvas.getHeight()));

        handler.updateCamera(new RectF(0, -height, canvas.getWidth(), canvas.getHeight()));

        PositioningSystem.sScale = 50;


        lFloor = new Layer(0, new RectF(0, 0, width, height), IsometricPositioningSystem.getInstance());
        {
            Image tile1 = new Image(getClass().getClassLoader().getResourceAsStream("assets/floor/isometric1.png"));
            Image tile2 = new Image(getClass().getClassLoader().getResourceAsStream("assets/floor/isometric2.png"));
            for (int i = 0; i < map.getWidth(); i++)
                for (int j = 0; j < map.getHeight(); j++)
                {
                    ImageDrawable drawable = new ImageDrawable((i + j) % 2 == 0 ? tile1 : tile2, 61);
                    drawable.setPivot(.5, .5);
                    Drawer drawer = new Drawer(drawable);
                    drawer.setPosition(i, j);
                    drawer.setLayer(lFloor);
                }
        }

        lObjects = new Layer(1, new RectF(0, 0, width, height), IsometricPositioningSystem.getInstance());
        {
            map.getAllBuildings().forEach(building ->
            {
                try
                {
                    BuildingDrawer drawer = new BuildingDrawer(building);
                    drawer.setLayer(lObjects);
                }
                catch (Exception ignored)
                {
                    ignored.printStackTrace();
                }
            });
        }

        gscene.addLayer(lFloor);
        gscene.addLayer(lObjects);
        handler.setScene(gscene);

        new GameLooper(handler).start();

        setScene(new Scene(group));
        show();
    }
}
