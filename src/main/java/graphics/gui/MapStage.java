package graphics.gui;

import graphics.GameLooper;
import graphics.GameScene;
import graphics.GraphicHandler;
import graphics.GraphicsValues;
import graphics.drawers.BuildingDrawer;
import graphics.drawers.Drawer;
import graphics.drawers.drawables.ImageDrawable;
import graphics.helpers.BuildingGraphicHelper;
import graphics.layers.Layer;
import graphics.positioning.IsometricPositioningSystem;
import graphics.positioning.NormalPositioningSystem;
import graphics.positioning.PositioningSystem;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import models.Map;
import models.buildings.Building;
import utils.GraphicsUtilities;
import utils.RectF;
import utils.SizeF;

import java.net.URISyntaxException;
import java.util.ArrayList;

public abstract class MapStage extends Stage
{
    private Map map;

    private GameLooper looper;

    protected ArrayList<GraphicHandler> graphicHandlers = new ArrayList<>();

    protected GraphicHandler gHandler;

    protected GameScene gScene;
    private final Layer lFloor;
    private final Layer lObjects;
    private final Layer lbackground;

    protected final double width;
    protected final double height;

    public MapStage(Map map, double width, double height)
    {
        this.map = map;

        this.width = width;
        this.height = height;

        PositioningSystem.sScale = 25;

        lFloor = new Layer(1, new RectF(0, 0, width, height), IsometricPositioningSystem.getInstance());
        lObjects = new Layer(2, new RectF(0, 0, width, height), IsometricPositioningSystem.getInstance());
        lbackground = new Layer(0, new RectF(0, 0, width, height), NormalPositioningSystem.getInstance());
    }

    public Map getMap()
    {
        return map;
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

        GraphicsValues.setScale(0.8);
        if (System.getProperty("os.name").equals("Linux"))
            GraphicsValues.setScale(1);

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

        gScene.addLayer(lbackground);
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
        try
        {
            ImageDrawable bg;
            ImageDrawable tile1;
            ImageDrawable tile2;
            if (this instanceof AttackStage)
                bg = GraphicsUtilities.createImageDrawable("assets/floor/background.png", PositioningSystem.sScale * IsometricPositioningSystem.ANG_COS * 30 * 2,
                        PositioningSystem.sScale * IsometricPositioningSystem.ANG_SIN * 30 * 2, true);
            else
                bg = GraphicsUtilities.createImageDrawable("assets/floor/background2.png", PositioningSystem.sScale * IsometricPositioningSystem.ANG_COS * 30 * 2,
                        PositioningSystem.sScale * IsometricPositioningSystem.ANG_SIN * 30 * 2, true);
            Drawer bgDrawer = new Drawer(bg);
            bgDrawer.setPosition(0, 0);
            bg.setPivot(0, 0.5);
            bgDrawer.setLayer(lbackground);

            if (this instanceof AttackStage)
            {
                tile1 = GraphicsUtilities.createImageDrawable("assets/floor/isometric3.png", IsometricPositioningSystem.sScale * IsometricPositioningSystem.ANG_COS * 2, IsometricPositioningSystem.sScale * IsometricPositioningSystem.ANG_SIN * 2, true);
                tile2 = GraphicsUtilities.createImageDrawable("assets/floor/isometric4.png", IsometricPositioningSystem.sScale * IsometricPositioningSystem.ANG_COS * 2, IsometricPositioningSystem.sScale * IsometricPositioningSystem.ANG_SIN * 2, true);
            }
            else
            {
                tile1 = GraphicsUtilities.createImageDrawable("assets/floor/isometric1.png", IsometricPositioningSystem.sScale * IsometricPositioningSystem.ANG_COS * 2, IsometricPositioningSystem.sScale * IsometricPositioningSystem.ANG_SIN * 2, true);
                tile2 = GraphicsUtilities.createImageDrawable("assets/floor/isometric2.png", IsometricPositioningSystem.sScale * IsometricPositioningSystem.ANG_COS * 2, IsometricPositioningSystem.sScale * IsometricPositioningSystem.ANG_SIN * 2, true);
            }
            tile1.setPivot(.5, .5);
            tile2.setPivot(.5, .5);
            for (int i = 0; i < map.getWidth(); i++)
                for (int j = 0; j < map.getHeight(); j++)
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

    private void addBuildings()
    {
        map.getAllBuildings().forEach(this::addBuilding);
    }

    public abstract BuildingGraphicHelper addBuilding(Building building);

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
