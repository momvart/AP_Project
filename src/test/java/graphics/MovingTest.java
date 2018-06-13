package graphics;

import graphics.drawers.Drawer;
import graphics.drawers.drawables.Drawable;
import graphics.drawers.drawables.ImageDrawable;
import graphics.drawers.drawables.animators.AlphaAnimator;
import graphics.gui.AttackStage;
import graphics.helpers.GraphicHelper;
import graphics.helpers.SoldierGraphicHelper;
import graphics.positioning.IsometricPositioningSystem;
import graphics.positioning.NormalPositioningSystem;
import graphics.positioning.PositioningSystem;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import models.World;
import models.attack.Attack;
import models.soldiers.Guardian;
import models.soldiers.SoldierFactory;
import utils.PointF;
import utils.RectF;
import utils.SizeF;

import java.util.stream.Collectors;

public class MovingTest extends Application
{
    public static void main(String[] args)
    {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception
    {
        GraphicsValues.setScale(1);
        World.initialize();
        Group group = new Group();
        double width = 600;
        double height = 400;
        Canvas canvas = new Canvas(1200, 800);

        group.getChildren().add(canvas);

        GraphicHandler handler = new GraphicHandler(canvas.getGraphicsContext2D(), new RectF(0, 0, canvas.getWidth(), canvas.getHeight()));
        GameScene gameScene = new GameScene(new SizeF(canvas.getWidth(), canvas.getHeight()));
        canvas.setOnMouseClicked(handler::handleMouseClick);

        handler.updateCamera(new RectF(0, -400, canvas.getWidth(), canvas.getHeight()));

        PositioningSystem.sScale = 50;
        Layer lFloor = new Layer(0, new RectF(0, 0, width, height), IsometricPositioningSystem.getInstance());
        Image img1 = new Image(getClass().getClassLoader().getResourceAsStream("assets/floor/isometric1.png"));
        Image img2 = new Image(getClass().getClassLoader().getResourceAsStream("assets/floor/isometric2.png"));
        for (int i = 0; i < 15; i++)
            for (int j = 0; j < 15; j++)
            {
                ImageDrawable drawable = new ImageDrawable((i + j) % 2 == 0 ? img1 : img2, 61);
                drawable.setPivot(.5, .5);
                Drawer drawer = new Drawer(drawable);
                drawer.setPosition(i, j);
                drawer.setLayer(lFloor);
            }


        Layer layer = new Layer(2, new RectF(0, 0, width, height), IsometricPositioningSystem.getInstance());

        ImageDrawable building = new ImageDrawable(new Image(getClass().getClassLoader().getResourceAsStream("assets/buildings/townhall/10/001.png")), 80);
        building.setPivot(.5, .7);
        building.setRotation(45);
        Drawer drawer = new Drawer(building);
        drawer.setPosition(7, 7);
        drawer.setClickListener(event -> System.out.println("Salam"));
        drawer.setLayer(layer);

        SoldierGraphicHelper helper = new SoldierGraphicHelper(SoldierFactory.createSoldierByTypeID(Guardian.SOLDIER_TYPE, 1));
        helper.getDrawer().setPosition(0, 5);
        helper.getDrawer().setLayer(layer);
        helper.getDrawer().setClickListener(event -> System.out.println("Soldier"));
        helper.moveTo(new PointF(0, 10));
        helper.setMoveListener(position ->
        {
            if (PointF.euclideanDistance(position, new PointF(0, 10)) < 0.01)
                helper.moveTo(new PointF(5, 10));
            else if (PointF.euclideanDistance(position, new PointF(5, 10)) < 0.01)
                helper.moveTo(new PointF(0, 5));
            else
                helper.moveTo(new PointF(0, 10));
        });
        handler.addUpdatable(helper);

        AlphaAnimator animator = new AlphaAnimator(1, true, helper.getDrawer().getAnimations().values().stream().map(animationDrawable -> (Drawable)animationDrawable).collect(Collectors.toList()), 0.5, 1);
        animator.start();
        handler.addUpdatable(animator);

        SoldierGraphicHelper helper2 = new SoldierGraphicHelper(SoldierFactory.createSoldierByTypeID(Guardian.SOLDIER_TYPE, 2));
        helper2.getDrawer().setPosition(8, 6);
        helper2.getDrawer().setLayer(layer);
//        helper2.makeIdle();
        helper2.moveTo(new PointF(6, 6));
        helper2.setMoveListener(position ->
        {
            if (PointF.euclideanDistance(position, new PointF(6, 6)) < 0.01)
                helper2.moveTo(new PointF(8, 6));
            else if (PointF.euclideanDistance(position, new PointF(8, 6)) < 0.01)
                helper2.moveTo(new PointF(6, 6));
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
