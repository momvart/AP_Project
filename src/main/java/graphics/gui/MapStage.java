package graphics.gui;

import graphics.*;
import graphics.drawers.BuildingDrawer;
import graphics.drawers.Drawer;
import graphics.drawers.drawables.ImageDrawable;
import graphics.helpers.AreaSplashDefenseGraphicHelper;
import graphics.helpers.BuildingGraphicHelper;
import graphics.helpers.SingleTDefenseGraphicHelper;
import graphics.positioning.IsometricPositioningSystem;
import graphics.positioning.PositioningSystem;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import models.Map;
import models.attack.attackHelpers.SingleTargetAttackHelper;
import models.buildings.Building;
import models.buildings.DefensiveTower;
import utils.GraphicsUtilities;
import utils.RectF;
import utils.SizeF;

import java.net.URISyntaxException;
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
            GraphicsValues.setScale(1.5);

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
        try
        {
            ImageDrawable tile1 = GraphicsUtilities.createImageDrawable("assets/floor/isometric1.png", IsometricPositioningSystem.sScale * IsometricPositioningSystem.ANG_COS * 2, IsometricPositioningSystem.sScale * IsometricPositioningSystem.ANG_SIN * 2, true);
            tile1.setPivot(.5, .5);
            ImageDrawable tile2 = GraphicsUtilities.createImageDrawable("assets/floor/isometric2.png", IsometricPositioningSystem.sScale * IsometricPositioningSystem.ANG_COS * 2, IsometricPositioningSystem.sScale * IsometricPositioningSystem.ANG_SIN * 2, true);
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

    protected void addBuilding(Building building)
    {
        BuildingGraphicHelper graphicHelper = new BuildingGraphicHelper(building, lObjects, map);

        setUpBuildingDrawer(graphicHelper.getBuildingDrawer());
        graphicHelper.setUpListeners();
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
