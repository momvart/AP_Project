package graphics.gui;

import graphics.GameLooper;
import graphics.GameScene;
import graphics.GraphicHandler;
import graphics.Layer;
import graphics.drawers.Drawer;
import graphics.drawers.drawables.ImageDrawable;
import graphics.helpers.SoldierGraphicHelper;
import graphics.positioning.IsometricPositioningSystem;
import graphics.positioning.PositioningSystem;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import models.attack.Attack;
import models.buildings.Building;
import models.soldiers.Soldier;
import utils.RectF;

import java.net.URISyntaxException;

public class AttackStage extends Stage
{
    private Attack attack;

    private GameLooper looper;
    private GraphicHandler handler;

    private GameScene gscene;
    private Layer lFloor;
    private Layer lObjects;

    public AttackStage(Attack attack)
    {
        this.attack = attack;
    }


    public void setUpAndShow()
    {
        final int width = 800, height = 800;

        StackPane root = new StackPane();

        Canvas canvas = new Canvas(width, height);
        root.getChildren().add(canvas);

        gscene = new GameScene(width, height);
        handler = new GraphicHandler(canvas.getGraphicsContext2D(), new RectF(0, 0, width, height));

        lFloor = new Layer(0, new RectF(0, height / 2, width, height), IsometricPositioningSystem.getInstance());
        {
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
        }

        lObjects = new Layer(1, new RectF(0, 0, width, height), IsometricPositioningSystem.getInstance());

        gscene.addLayer(lFloor);
        gscene.addLayer(lObjects);

        looper = new GameLooper(handler);

        attack.setSoldierPutListener(this::addSoldier);

        setScene(new Scene(root, width, height));
        show();
    }

    private void addBuilding(Building building)
    {

    }

    private void addSoldier(Soldier soldier)
    {
        try
        {
            SoldierGraphicHelper helper = new SoldierGraphicHelper(soldier);
            helper.makeIdle();
            helper.getDrawer().setLayer(lObjects);
            soldier.getAttackHelper().setGraphicHelper(helper);
            handler.addUpdatable(helper);
        }
        catch (URISyntaxException e) { e.printStackTrace(); }
    }

}
