package graphics;

import javafx.application.Application;

import graphics.drawers.*;
import graphics.drawers.drawables.*;
import graphics.positioning.NormalPositioningSystem;
import javafx.scene.*;
import javafx.scene.canvas.Canvas;
import javafx.scene.paint.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import utils.*;

public class BasicTextTest extends Application
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
        Layer layer = new Layer(0, new RectF(0, 0, 400, 400), new NormalPositioningSystem(10));

        Fonts.initialize();

        Drawer drawer = new Drawer(new TextDrawable("Salam", Color.BLACK, Font.font(20)));
        drawer.setPosition(1, 2);
        drawer.setLayer(layer);

        VProgressbarDrawable progressbar = new VProgressbarDrawable(new LinearGradient(0, 0, 0, 1, true, CycleMethod.NO_CYCLE, new Stop(0, Color.GOLD), new Stop(1, Color.ORANGE)));
        Drawer drawer2 = new Drawer(progressbar);
        progressbar.setSize(100, 10);
        progressbar.setProgress(.75);
        progressbar.setStroke(Color.BROWN);
        drawer2.setPosition(1, 10);
        drawer2.setLayer(layer);

        VProgressbarDrawable progressbar2 = new VProgressbarDrawable(new LinearGradient(0, 0, 0, 1, true, CycleMethod.NO_CYCLE, new Stop(0, Color.VIOLET), new Stop(1, Color.PURPLE)));
        Drawer drawer3 = new Drawer(progressbar2);
        progressbar2.setSize(100, 10);
        progressbar2.setProgress(.50);
        progressbar2.setStroke(Color.BROWN);
        drawer3.setPosition(1, 15);
        drawer3.setLayer(layer);

        gameScene.addLayer(layer);
        handler.setScene(gameScene);

        new GameLooper(handler).start();

        primaryStage.setScene(new Scene(group, 400, 400));
        primaryStage.show();
    }
}
