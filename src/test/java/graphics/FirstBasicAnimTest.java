package graphics;

import graphics.drawers.*;
import graphics.drawers.drawables.*;
import javafx.application.Application;
import javafx.scene.*;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
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

        GraphicHandler handler = new GraphicHandler(canvas.getGraphicsContext2D(), new RectF(0, 0, 400, 400));
        GameScene gameScene = new GameScene(new SizeF(400, 400));
        Layer layer = new Layer(0, new RectF(0, 0, 400, 400));

        AnimationDrawer drawer1 = new AnimationDrawer(new ImageDrawable(null));
        {
            ImageDrawable[] imgs = new ImageDrawable[16];
            for (int i = 1; i <= imgs.length; i++)
                imgs[i - 1] = new ImageDrawable(new Image(getClass().getClassLoader().getResourceAsStream("assets/soldiers/guardian/idle/" + Integer.toString(i) + ".png")), 100, 100);

            FrameAnimationDrawable anim = new FrameAnimationDrawable(imgs, 1);
            drawer1.addAnimation("idle", anim);
            drawer1.playAnimation("idle");
            drawer1.setPosition(imgs[0].getSize().getWidth() / 2, imgs[0].getSize().getHeight() / 2);
            layer.addObject(drawer1);
        }
        AnimationDrawer drawer2 = new AnimationDrawer(new ImageDrawable(null));
        {
            ImageDrawable[] imgs = new ImageDrawable[16];
            for (int i = 1; i <= imgs.length; i++)
                imgs[i - 1] = new ImageDrawable(new Image(getClass().getClassLoader().getResourceAsStream("assets/soldiers/guardian/run/" + Integer.toString(i) + ".png")), 100, 100);

            FrameAnimationDrawable anim = new FrameAnimationDrawable(imgs, 0.5);
            anim.setScale(-1, 1);
            drawer2.addAnimation("run", anim);
            drawer2.playAnimation("run");
            drawer2.setPosition(imgs[0].getSize().getWidth() / 2 + 100, imgs[0].getSize().getHeight() / 2);
            layer.addObject(drawer2);
        }
        AnimationDrawer drawer3 = new AnimationDrawer(new ImageDrawable(null));
        {
            ImageDrawable[] imgs = new ImageDrawable[12];
            for (int i = 1; i <= imgs.length; i++)
                imgs[i - 1] = new ImageDrawable(new Image(getClass().getClassLoader().getResourceAsStream("assets/soldiers/guardian/attack/" + Integer.toString(i) + ".png")), 100, 100);

            FrameAnimationDrawable anim = new FrameAnimationDrawable(imgs, 0.5);
            anim.setRotation(180);
            drawer3.addAnimation("attack", anim);
            drawer3.playAnimation("attack");
            drawer3.setPosition(imgs[0].getSize().getWidth() / 2 + 200, imgs[0].getSize().getHeight() / 2);
            layer.addObject(drawer3);
        }
        AnimationDrawer drawer4 = new AnimationDrawer(new ImageDrawable(null));
        {
            ImageDrawable[] imgs = new ImageDrawable[12];
            for (int i = 1; i <= imgs.length; i++)
                imgs[i - 1] = new ImageDrawable(new Image(getClass().getClassLoader().getResourceAsStream("assets/soldiers/guardian/die/" + Integer.toString(i) + ".png")), 100, 100);

            FrameAnimationDrawable anim = new FrameAnimationDrawable(imgs, 1.5);
            drawer4.addAnimation("die", anim);
            drawer4.playAnimation("die");
            drawer4.setPosition(imgs[0].getSize().getWidth() / 2 + 300, imgs[0].getSize().getHeight() / 2);
            layer.addObject(drawer4);
        }


        gameScene.addLayer(layer);
        handler.setScene(gameScene);

        new GameLooper(handler).start();

        primaryStage.setScene(new Scene(group, 400, 400));
        primaryStage.show();
    }
}
