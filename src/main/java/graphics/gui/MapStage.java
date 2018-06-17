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
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import models.Map;
import utils.RectF;
import utils.SizeF;

import java.util.ArrayList;

public class MapStage extends Stage
{
    private Map map;

    private GameLooper looper;

    protected ArrayList<GraphicHandler> graphicHandlers = new ArrayList<>();

    protected GraphicHandler gHandler;

    protected GameScene gScene;
    private final Layer lFloor;
    private final Layer lObjects;

    protected final double width;
    protected final double height;

    public MapStage(Map map, double width, double height)
    {
        this.map = map;

        this.width = width;
        this.height = height;

        PositioningSystem.sScale = 25;

        lFloor = new Layer(0, new RectF(0, 0, width, height), IsometricPositioningSystem.getInstance());
        lObjects = new Layer(1, new RectF(0, 0, width, height), IsometricPositioningSystem.getInstance());
    }


    public Layer getFloorLayer()
    {
        return lFloor;
    }

    public Layer getObjectsLayer()
    {
        return lObjects;
    }

    public void setUpAndShow()
    {
        Group group = new Group();

        GraphicsValues.setScale(0.5);
        if (System.getProperty("os.name").equals("Linux"))
            GraphicsValues.setScale(2);

        Canvas canvas = new Canvas(width * GraphicsValues.getScale(), height * GraphicsValues.getScale());
        group.getChildren().add(canvas);

        gHandler = new GraphicHandler(canvas.getGraphicsContext2D(),
                new RectF(
                        -(canvas.getWidth() - PositioningSystem.sScale * GraphicsValues.getScale() * IsometricPositioningSystem.ANG_COS * 30 * 2) / 2,
                        -(canvas.getHeight() - PositioningSystem.sScale * GraphicsValues.getScale() * IsometricPositioningSystem.ANG_SIN * 30 * 2) / 2 - (PositioningSystem.sScale * GraphicsValues.getScale() * IsometricPositioningSystem.ANG_SIN * 30 * 2) / 2,
                        canvas.getWidth(), canvas.getHeight()));
        gScene = new GameScene(new SizeF(width, height));

        setUpFloor();

        addBuildings();

        gScene.addLayer(lFloor);
        gScene.addLayer(lObjects);

        gHandler.setScene(gScene);
        graphicHandlers.add(gHandler);

        new GameLooper(gHandler).start();
        preShow(group);
        group.setOnMouseClicked(this::handleMouseClick);
        setScene(new Scene(group));
        show();
    }

    private void setUpFloor()
    {
        Image tile1 = new Image(getClass().getClassLoader().getResourceAsStream("assets/floor/isometric1.png"));
        Image tile2 = new Image(getClass().getClassLoader().getResourceAsStream("assets/floor/isometric2.png"));
        for (int i = 0; i < map.getWidth(); i++)
            for (int j = 0; j < map.getHeight(); j++)
            {
                ImageDrawable drawable = new ImageDrawable((i + j) % 2 == 0 ? tile1 : tile2, IsometricPositioningSystem.ANG_SIN * 2 * PositioningSystem.sScale);
                drawable.setPivot(.5, .5);
                Drawer drawer = new Drawer(drawable);
                drawer.setPosition(i, j);
                drawer.setLayer(lFloor);
            }
    }

    private void addBuildings()
    {
        map.getAllBuildings().forEach(building ->
        {
            try
            {
                BuildingDrawer drawer = new BuildingDrawer(building);
                drawer.setLayer(lObjects);
                setUpBuildingDrawer(drawer);
            }
            catch (Exception ignored) { ignored.printStackTrace(); }
        });
    }

    protected void setUpBuildingDrawer(BuildingDrawer drawer)
    { }

    protected void preShow(Group group)
    {
    }

    private void handleMouseClick(MouseEvent event)
    {
        for (int i = graphicHandlers.size() - 1; i >= 0; i--)
            if (graphicHandlers.get(i).handleMouseClickResult(event))
                return;
    }
}
