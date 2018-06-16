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

public class AttackStage extends MapStage
{
    private Attack attack;

    public AttackStage(Attack attack, double width, double height)
    {
        super(attack.getMap(), width, height);
        this.attack = attack;
    }


    public void setUpAndShow()
    {
        attack.setSoldierPutListener(this::addSoldier);

        super.setUpAndShow();
    }

    private void addSoldier(Soldier soldier)
    {
        SoldierGraphicHelper helper = new SoldierGraphicHelper(soldier, getObjectsLayer());
        helper.makeIdle();
        soldier.getAttackHelper().setGraphicHelper(helper);
        gHandler.addUpdatable(helper);
    }
}
