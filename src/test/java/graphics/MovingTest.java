package graphics;

import graphics.drawers.Drawer;
import graphics.drawers.drawables.Drawable;
import graphics.drawers.drawables.ImageDrawable;
import graphics.drawers.drawables.animators.AlphaAnimator;
import graphics.helpers.SoldierGraphicHelper;
import graphics.positioning.IsometricPositioningSystem;
import graphics.positioning.PositioningSystem;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import models.World;
import models.soldiers.Guardian;
import models.soldiers.SoldierFactory;
import utils.PointF;
import utils.RectF;
import utils.SizeF;

import java.util.ArrayList;

public class MovingTest extends Application
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

        ArrayList<Drawable> alphaTargets = new ArrayList<>();
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
                if (i == 0 || j == 0 || i == 29 || j == 29)
                    alphaTargets.add(drawable);
            }
        AlphaAnimator animator = new AlphaAnimator(1, true, alphaTargets, 0.5, 1);
        animator.start();
        handler.addUpdatable(animator);

        Layer layer = new Layer(2, new RectF(0, 0, width, height), IsometricPositioningSystem.getInstance());

        ImageDrawable building = new ImageDrawable(new Image(getClass().getClassLoader().getResourceAsStream("assets/buildings/wizard tower/1/001.png")), 80);
        building.setPivot(.5, .7);
        Drawer drawer = new Drawer(building);
        drawer.setPosition(7, 7);
        drawer.setClickListener((sender, event) -> System.out.println("Salam"));
        drawer.setLayer(layer);

        SoldierGraphicHelper helper = new SoldierGraphicHelper(SoldierFactory.createSoldierByTypeID(Guardian.SOLDIER_TYPE, 1), layer);
        helper.getDrawer().setPosition(0, 5);
        helper.getDrawer().setClickListener((sender, event) -> System.out.println("Soldier"));
        helper.startJoggingToward(new PointF(0, 10));
        helper.setMoveListener(position ->
        {
            if (PointF.euclideanDistance(position, new PointF(0, 10)) < 0.01)
                helper.startJoggingToward(new PointF(5, 10));
            else if (PointF.euclideanDistance(position, new PointF(5, 10)) < 0.01)
                helper.startJoggingToward(new PointF(0, 5));
            else
                helper.startJoggingToward(new PointF(0, 10));
        });
        handler.addUpdatable(helper);

        SoldierGraphicHelper helper2 = new SoldierGraphicHelper(SoldierFactory.createSoldierByTypeID(Guardian.SOLDIER_TYPE, 2), layer);
        helper2.getDrawer().setPosition(8, 6);
//        helper2.makeIdle();
        helper2.startJoggingToward(new PointF(6, 6));
        helper2.setMoveListener(position ->
        {
            if (PointF.euclideanDistance(position, new PointF(6, 6)) < 0.01)
                helper2.startJoggingToward(new PointF(8, 6));
            else if (PointF.euclideanDistance(position, new PointF(8, 6)) < 0.01)
                helper2.startJoggingToward(new PointF(6, 6));
        });
        handler.addUpdatable(helper2);

        gameScene.addLayer(lFloor);
        gameScene.addLayer(layer);
        handler.setScene(gameScene);

        new GameLooper(handler).start();

        primaryStage.setScene(new Scene(group, canvas.getWidth(), canvas.getHeight()));
        primaryStage.show();

    }
}
