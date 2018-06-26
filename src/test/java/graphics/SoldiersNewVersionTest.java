package graphics;

import graphics.drawers.AnimationDrawer;
import graphics.drawers.drawables.FrameAnimationDrawable;
import graphics.layers.Layer;
import graphics.positioning.NormalPositioningSystem;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.stage.Stage;
import utils.GraphicsUtilities;
import utils.RectF;
import utils.SizeF;

public class SoldiersNewVersionTest extends Application
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
        Layer layer = new Layer(0, new RectF(0, 0, 400, 400), new NormalPositioningSystem(10));

        FrameAnimationDrawable anim = new FrameAnimationDrawable(GraphicsUtilities.createFramesFrom("assets/soldiers/healer/002/run", -1, 0.5, 0.5), 0.5);
        anim.setReversible(true);
        anim.setScale(-0.5, 0.5);
        AnimationDrawer drawer = new AnimationDrawer(null);
        drawer.addAnimation("run", anim);
        drawer.playAnimation("run");
        drawer.setPosition(20, 20);
        drawer.setLayer(layer);

        gameScene.addLayer(layer);
        handler.setScene(gameScene);

        new GameLooper(handler).start();

        primaryStage.setScene(new Scene(group, 400, 400));
        primaryStage.show();
    }
}