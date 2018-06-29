package graphics.gui;

import exceptions.ConsoleException;
import graphics.GameLooper;
import graphics.GameScene;
import graphics.GraphicHandler;
import graphics.GraphicsValues;
import graphics.helpers.*;
import graphics.layers.MenuLayer;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import menus.ParentMenu;
import menus.SoldierMenuItem;
import models.World;
import models.attack.Attack;
import models.attack.attackHelpers.SingleTargetAttackHelper;
import models.buildings.Building;
import models.buildings.DefensiveTower;
import models.soldiers.Healer;
import models.soldiers.Soldier;
import models.soldiers.SoldierValues;
import utils.Point;
import utils.RectF;

import java.util.ArrayList;

public class AttackStage extends MapStage
{
    private Attack attack;
    private MenuLayer lmenu;
    private final double CELL_SIZE = height / 10;
    private Canvas guiCanvas;
    private GameLooper guiLooper;
    private GameScene guiScene;
    private GraphicHandler guiHandler;

    public AttackStage(Attack attack, double width, double height)
    {
        super(attack.getMap(), width, height);
        this.attack = attack;
        lmenu = new MenuLayer(6, new RectF(0, height - CELL_SIZE - GraphicsValues.PADDING, width, CELL_SIZE), MenuLayer.Orientation.HORIZONTAL);
    }

    @Override
    protected void preShow(Group group)
    {
        super.preShow(group);

        guiCanvas = new Canvas(width * GraphicsValues.getScale(), height * GraphicsValues.getScale());
        group.getChildren().add(guiCanvas);

        guiHandler = new GraphicHandler(guiCanvas.getGraphicsContext2D(), new RectF(0, 0, guiCanvas.getWidth(), guiCanvas.getHeight()));
        guiScene = new GameScene(width, height);

        lmenu.setItemCellSize(CELL_SIZE);

        for (int i = 1; i <= SoldierValues.SOLDIER_TYPES_COUNT; i++)
            attack.addUnits(World.getVillage().getSoldiers(i));

        ParentMenu parentMenu = new ParentMenu(100, "");
        ArrayList<SoldierMenuItem> soldierMenuItems = new ArrayList<>();
        for (int i = 1; i <= SoldierValues.SOLDIER_TYPES_COUNT; i++)
        {
            SoldierMenuItem submenu = new SoldierMenuItem(100 + i, SoldierValues.getSoldierInfo(i).getName(), attack.getAllUnits(i).size(), i);
            soldierMenuItems.add(submenu);
        }
        soldierMenuItems.forEach(parentMenu::insertItem);
        lmenu.setClickListener(item ->
        {
            try
            {
                attack.putUnits(item.getId() - 100, 1, new Point(0, 0));
            }
            catch (ConsoleException e)
            {
                e.printStackTrace();
            }
        });
        lmenu.setCurrentMenu(parentMenu);

        guiScene.addLayer(lmenu);

        guiHandler.setScene(guiScene);

        new GameLooper(guiHandler).start();

        graphicHandlers.add(guiHandler);
    }

    public void setUpAndShow()
    {
        attack.setSoldierPutListener(this::addSoldier);

        super.setUpAndShow();
    }

    @Override
    public BuildingGraphicHelper addBuilding(Building building)
    {
        AttackBuildingGraphicHelper graphicHelper;

        if (building instanceof DefensiveTower)
        {
            if (building.getAttackHelper() instanceof SingleTargetAttackHelper)
                graphicHelper = new SingleTDefenseGraphicHelper(building, getObjectsLayer(), getMap());
            else
                graphicHelper = new AreaSplashDefenseGraphicHelper(building, getObjectsLayer(), getMap());
        }
        else
            graphicHelper = new AttackBuildingGraphicHelper(building, getObjectsLayer(), getMap());

        building.getAttackHelper().setGraphicHelper(graphicHelper);
        setUpBuildingDrawer(graphicHelper.getBuildingDrawer());
        graphicHelper.setUpListeners();
        gHandler.addUpdatable(graphicHelper);

        return graphicHelper;
    }

    private void addSoldier(Soldier soldier)
    {
        SoldierGraphicHelper helper;
        if (soldier.getType() == Healer.SOLDIER_TYPE)
        {
            helper = new HealerGraphicHelper(soldier, getObjectsLayer());
        }
        else
        {
            helper = new GeneralSoldierGraphicHelper(soldier, getObjectsLayer());
        }

        soldier.getAttackHelper().setGraphicHelper(helper);
        helper.setUpListeners();
        gHandler.addUpdatable(helper);
    }


}
