package graphics.gui;

import exceptions.ConsoleException;
import graphics.*;
import graphics.drawers.Drawer;
import graphics.drawers.drawables.ButtonDrawable;
import graphics.drawers.drawables.ImageDrawable;
import graphics.drawers.drawables.RoundRectDrawable;
import graphics.drawers.drawables.TextDrawable;
import graphics.helpers.*;
import graphics.layers.Layer;
import graphics.layers.ResourceLayer;
import graphics.positioning.IsometricPositioningSystem;
import graphics.positioning.NormalPositioningSystem;
import graphics.positioning.PositioningSystem;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import menus.Menu;
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
import utils.GraphicsUtilities;
import utils.Point;
import utils.RectF;
import utils.TimeSpan;

import java.net.URISyntaxException;
import java.util.ArrayList;

public class AttackStage extends GUIMapStage
{
    private Attack theAttack;

    private ResourceLayer lResource;

    private TimerGraphicHelper timer;

    public AttackStage(Attack attack, double width, double height)
    {
        super(attack.getMap(), width, height);
        this.theAttack = attack;

        lResource = new ResourceLayer(8,
                new RectF(width - 200 - GraphicsValues.PADDING * 2, 20, 200 + GraphicsValues.PADDING * 2, 70),
                attack::getClaimedResource, attack::getTotalResource);
    }

    @Override
    protected void preShow(Group group)
    {
        super.preShow(group);

        initStyle(StageStyle.UNDECORATED);

        for (int i = 1; i <= SoldierValues.SOLDIER_TYPES_COUNT; i++)
            theAttack.addUnits(World.getVillage().getSoldiers(i));

        ParentMenu parentMenu = new ParentMenu(100, "");
        ArrayList<SoldierMenuItem> soldierMenuItems = new ArrayList<>();
        for (int i = 1; i <= SoldierValues.SOLDIER_TYPES_COUNT; i++)
        {
            SoldierMenuItem submenu = new SoldierMenuItem(100 + i, SoldierValues.getSoldierInfo(i).getName(), theAttack.getAllUnits(i).size(), i);
            soldierMenuItems.add(submenu);
        }
        soldierMenuItems.forEach(parentMenu::insertItem);
        getMenuLayer().setCurrentMenu(parentMenu);

        TextDrawable txtTime = new TextDrawable("", Color.WHITE, Fonts.getMedium());
        {
            txtTime.setHasShadow(true);
            txtTime.setPivot(0.5, 0.5);
            Drawer dTime = new Drawer(txtTime);
            dTime.setPosition(0.5, 0.5);
            dTime.setLayer(getStuffsLayer());
            timer = new TimerGraphicHelper(txtTime, new TimeSpan(3, 0), true);
            timer.setOnTimeFinished(() -> this.quitAttack(Attack.QuitReason.TURN));
            getGuiHandler().addUpdatable(timer);
        }

        ButtonDrawable btnEndAttack = new ButtonDrawable("End Attack", GraphicsValues.IconPaths.Stop, CELL_SIZE, CELL_SIZE);
        btnEndAttack.setPivot(0, 0);
        btnEndAttack.setFill(ButtonDrawable.DARK);
        Drawer dBtnEndAttack = new Drawer(btnEndAttack);
        dBtnEndAttack.setPosition(0, getStuffsLayer().getHeight() / ((NormalPositioningSystem)getStuffsLayer().getPosSys()).getScale() - 2);
        dBtnEndAttack.setLayer(getStuffsLayer());
        dBtnEndAttack.setClickListener((sender, event) -> quitAttack(Attack.QuitReason.USER));


        getGuiScene().addLayer(lResource);
    }

    public void setUpAndShow()
    {
        theAttack.setSoldierPutListener(this::addSoldier);

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
        building.getAttackHelper().addDestroyListener(this::checkForAllBuildingsDestroyed);
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
        soldier.getAttackHelper().addSoldierDieListener(this::checkForAllBuildingsDead);
        helper.setUpListeners();
        gHandler.addUpdatable(helper);
    }

    @Override
    protected void setUpFloor()
    {
        try
        {
            ImageDrawable bg;
            ImageDrawable tile1;
            ImageDrawable tile2;
            bg = GraphicsUtilities.createImageDrawable("assets/floor/background.png", PositioningSystem.sScale * IsometricPositioningSystem.ANG_COS * 30 * 2,
                    PositioningSystem.sScale * IsometricPositioningSystem.ANG_SIN * 30 * 2, true);
            Drawer bgDrawer = new Drawer(bg);
            bgDrawer.setPosition(0, 0);
            bg.setPivot(0, 0.5);
            bgDrawer.setLayer(lbackground);

            tile1 = GraphicsUtilities.createImageDrawable("assets/floor/isometric3.png", IsometricPositioningSystem.sScale * IsometricPositioningSystem.ANG_COS * 2, IsometricPositioningSystem.sScale * IsometricPositioningSystem.ANG_SIN * 2, true);
            tile2 = GraphicsUtilities.createImageDrawable("assets/floor/isometric4.png", IsometricPositioningSystem.sScale * IsometricPositioningSystem.ANG_COS * 2, IsometricPositioningSystem.sScale * IsometricPositioningSystem.ANG_SIN * 2, true);
            tile1.setPivot(.5, .5);
            tile2.setPivot(.5, .5);
            for (int i = 0; i < map.getWidth(); i++)
                for (int j = 0; j < map.getHeight(); j++)
                {
                    Drawer drawer = new Drawer((i + j) % 2 == 0 ? tile1 : tile2);
                    drawer.setPosition(i, j);
                    int I = i;
                    int J = j;
                    drawer.setClickListener((sender, event) ->
                    {
                        SoldierMenuItem menu = (SoldierMenuItem)getMenuLayer().getCurrentMenu().getMenuItems().stream().filter(Menu::isFocused).findFirst().orElse(null);
                        if (menu == null)
                            return;
                        try
                        {
                            theAttack.putUnits(menu.getId() - 100, 1, new Point(I, J));
                            menu.setCount((int)theAttack.getAliveUnits(menu.getId() - 100).filter(soldier -> !soldier.getAttackHelper().isSoldierDeployed()).count());
                            getMenuLayer().updateMenu();
                        }
                        catch (ConsoleException e) { e.printStackTrace(); }
                    });
                    drawer.setLayer(lFloor);
                }
        }
        catch (URISyntaxException e)
        {
            showInfo(e.getMessage());
        }
    }

    private void checkForAllBuildingsDead()
    {
        if (theAttack.areSoldiersDead())
            quitAttack(Attack.QuitReason.SOLDIERS_DIE);
    }

    private void checkForAllBuildingsDestroyed()
    {
        if (theAttack.areBuildingsDestroyed())
            quitAttack(Attack.QuitReason.MAP_DESTROYED);
    }

    private void quitAttack(Attack.QuitReason reason)
    {
        getGuiHandler().removeUpdatable(timer);

        theAttack.quitAttack(reason);

        getLooper().stop();

        createAttackFinishLayer(reason);
    }

    private void createAttackFinishLayer(Attack.QuitReason reason)
    {
        Layer layer = new Layer(12, new RectF((width - 400) / 2, (height - 400) / 2, 400, 400), new NormalPositioningSystem(1));

        RoundRectDrawable bg = new RoundRectDrawable(layer.getWidth(), layer.getHeight(), GraphicsValues.PADDING, ButtonDrawable.DARK);
        Drawer dBackground = new Drawer(bg);
        dBackground.setPosition(0, 0);
        dBackground.setLayer(layer);

        TextDrawable txtTitle = new TextDrawable(reason.getTitle(), reason == Attack.QuitReason.MAP_DESTROYED ? Color.GREEN : Color.RED, Fonts.getLarge());
        {
            txtTitle.setHasShadow(true);
            txtTitle.setPivot(0.5, 0.5);
            Drawer drawer = new Drawer(txtTitle);
            drawer.setPosition(layer.getWidth() / 2, 50);
            drawer.setLayer(layer);
        }


        addTextAndIcon(layer, Integer.toString(theAttack.getClaimedResource().getGold()), GraphicsValues.IconPaths.GoldCoin, 100);
        addTextAndIcon(layer, Integer.toString(theAttack.getClaimedResource().getElixir()), GraphicsValues.IconPaths.ElixirDrop, 150);
        addTextAndIcon(layer, Integer.toString(theAttack.getClaimedScore()), GraphicsValues.IconPaths.Trophie, 200);

        ButtonDrawable btnEnd = new ButtonDrawable("Return to Village", GraphicsValues.IconPaths.Map, CELL_SIZE * 2, CELL_SIZE);
        btnEnd.setPivot(0.5, 1);
        btnEnd.setFill(Color.web("#41c300", 0.6));
        Drawer dBtnEnd = new Drawer(btnEnd);
        dBtnEnd.setPosition(layer.getWidth() / 2, layer.getHeight() - GraphicsValues.PADDING);
        dBtnEnd.setLayer(layer);
        dBtnEnd.setClickListener((sender, event) -> close());

        getGuiScene().addLayer(layer);
    }

    private void addTextAndIcon(Layer layer, String text, String iconPath, double y)
    {
        TextDrawable txt = new TextDrawable(text, Color.WHITE, Fonts.getLarge());
        txt.setHasShadow(true);
        txt.setMaxWidth(200);
        txt.setPivot(0.5, 0.5);

        Drawer drawer = new Drawer(txt);
        drawer.setPosition(layer.getWidth() / 2, y);
        drawer.setLayer(layer);

        try
        {
            ImageDrawable imgIcon = GraphicsUtilities.createImageDrawable(iconPath, 30, 30, true);
            imgIcon.setPivot(0.5, 0.5);
            Drawer dIcon = new Drawer(imgIcon);
            dIcon.setPosition(layer.getWidth() / 2 + txt.getMaxWidth() / 2, drawer.getPosition().getY());
            dIcon.setLayer(layer);
        }
        catch (URISyntaxException e) { }
    }
}
