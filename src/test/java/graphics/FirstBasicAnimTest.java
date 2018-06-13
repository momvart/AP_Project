package graphics;

import graphics.drawers.*;
import graphics.drawers.drawables.*;
import graphics.positioning.*;
import javafx.application.Application;
import javafx.scene.*;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import utils.*;

public class FirstBasicAnimTest extends Application
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

        Button btn1 = new Button();
        Button btn2 = new Button();
        Button btn3 = new Button();
        btn2.setLayoutX(100);
        btn3.setLayoutX(200);
        group.getChildren().add(btn1);
        group.getChildren().add(btn2);
        group.getChildren().add(btn3);
        GraphicHandler handler = new GraphicHandler(canvas.getGraphicsContext2D(), new RectF(0, 0, 400, 400));
        GameScene gameScene = new GameScene(new SizeF(400, 400));
        PositioningSystem.sScale = 50;
        Layer layer = new Layer(0, new RectF(0, 0, 400, 400), IsometricPositioningSystem.getInstance());

        AnimationDrawer drawer1 = new AnimationDrawer(new ImageDrawable(null));
        {
            FrameAnimationDrawable anim = GraphicsUtilities.createFrameAnimDrawableFrom(getClass().getClassLoader().getResource("assets/soldiers/guardian/1/idle").toURI(), 0.75, 100);
            drawer1.addAnimation("idle", anim);
            drawer1.addAnimation("run", GraphicsUtilities.createFrameAnimDrawableFrom(getClass().getClassLoader().getResource("assets/soldiers/guardian/1/run").toURI(), 0.75, 100));
            drawer1.addAnimation("attack", GraphicsUtilities.createFrameAnimDrawableFrom(getClass().getClassLoader().getResource("assets/soldiers/guardian/1/attack").toURI(), 0.75, 100));
            drawer1.playAnimation("idle");
            drawer1.setPosition(0, 0);
            drawer1.setLayer(layer);
            btn1.setOnAction(event -> drawer1.playAnimation("idle"));
            btn2.setOnAction(event -> drawer1.playAnimation("run"));
            btn3.setOnAction(event -> drawer1.playAnimation("attack"));
        }
//        AnimationDrawer drawer5 = new AnimationDrawer(new ImageDrawable(null));
//        {
//            FrameAnimationDrawable anim = GraphicsUtilities.createFrameAnimDrawableFrom(getClass().getClassLoader().getResource("assets/soldiers/archer/idle").toURI(), 0.75, 100);
//            anim.setScale(.75,.75);
//            drawer5.addAnimation("idle", anim);
//            drawer5.playAnimation("idle");
//            drawer5.setPosition(1, 1);
//            drawer5.setLayer(layer);
//        }

        gameScene.addLayer(layer);
        handler.setScene(gameScene);

        new GameLooper(handler).start();

        primaryStage.setScene(new Scene(group, 400, 400));
        primaryStage.show();
    }
}
