package graphics;

import graphics.drawers.Drawer;
import graphics.drawers.drawables.ImageDrawable;
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
import models.soldiers.Guardian;
import models.soldiers.SoldierFactory;
import utils.PointF;
import utils.RectF;
import utils.SizeF;

public class MovingTest extends Application
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
        Canvas canvas = new Canvas(800, 800);
        group.getChildren().add(canvas);

        GraphicHandler handler = new GraphicHandler(canvas.getGraphicsContext2D(), new RectF(0, 0, 800, 800));
        GameScene gameScene = new GameScene(new SizeF(800, 800));

        PositioningSystem.sScale = 50;
        Layer lFloor = new Layer(0, new RectF(0, 0, 800, 800), IsometricPositioningSystem.getInstance());
        Image img1 = new Image(getClass().getClassLoader().getResourceAsStream("assets/floor/isometric1.png"));
        Image img2 = new Image(getClass().getClassLoader().getResourceAsStream("assets/floor/isometric2.png"));
        {
            ImageDrawable drawable = new ImageDrawable(img2, 10);
            drawable.setPivot(.5, .5);
            Drawer drawer = new Drawer(drawable);
            drawer.setPosition(0, 0);
            drawer.setLayer(lFloor);
        }
        for (int i = 0; i < 30; i++)
            for (int j = 0; j < 30; j++)
            {
                ImageDrawable drawable = new ImageDrawable((i + j) % 2 == 0 ? img1 : img2, PositioningSystem.sScale);
                drawable.setPivot(.5, .5);
                Drawer drawer = new Drawer(drawable);
                drawer.setPosition(i, j);
                drawer.setLayer(lFloor);
            }


        Layer layer = new Layer(1, new RectF(0, 0, 800, 800), IsometricPositioningSystem.getInstance());

        SoldierGraphicHelper helper = new SoldierGraphicHelper(SoldierFactory.createSoldierByTypeID(Guardian.SOLDIER_TYPE, 1));
        helper.getDrawer().setPosition(0, 0);
        helper.getDrawer().setLayer(layer);
        helper.moveTo(new PointF(0, 10));
        helper.setMoveListener(position -> helper.getDrawer().playAnimation("idle"));
        handler.addUpdatable(helper);

        gameScene.addLayer(lFloor);
        gameScene.addLayer(layer);
        handler.setScene(gameScene);

        new GameLooper(handler).start();

        primaryStage.setScene(new Scene(group, 800, 800));
        primaryStage.show();
    }
}
