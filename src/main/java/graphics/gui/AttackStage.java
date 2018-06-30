package graphics.gui;

import exceptions.ConsoleException;
import graphics.*;
import graphics.drawers.Drawer;
import graphics.drawers.drawables.ImageDrawable;
import graphics.drawers.drawables.RoundRectDrawable;
import graphics.drawers.drawables.TextDrawable;
import graphics.helpers.*;
import graphics.layers.MenuLayer;
import graphics.layers.ResourceLayer;
import graphics.layers.ToastLayer;
import graphics.positioning.IsometricPositioningSystem;
import graphics.positioning.PositioningSystem;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.paint.Color;
import menus.Menu;
import menus.ParentMenu;
import menus.SoldierMenuItem;
import models.Resource;
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
    private Attack attack;

    private ResourceLayer lResource;

    private TimerGraphicHelper timer;

    public AttackStage(Attack attack, double width, double height)
    {
        super(attack.getMap(), width, height);
        this.attack = attack;

        lResource = new ResourceLayer(8,
                new RectF(width - 200 - GraphicsValues.PADDING * 2, 20, 200 + GraphicsValues.PADDING * 2, 70),
                attack::getClaimedResource, attack::getTotalResource);
    }

    @Override
    protected void preShow(Group group)
    {
        super.preShow(group);

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
        getMenuLayer().setCurrentMenu(parentMenu);

        TextDrawable txtTime = new TextDrawable("", Color.WHITE, Fonts.getMedium());
        {
            txtTime.setHasShadow(true);
            txtTime.setPivot(0.5, 0.5);
            Drawer dTime = new Drawer(txtTime);
            dTime.setPosition(0.5, 0.5);
            dTime.setLayer(getStuffsLayer());
            timer = new TimerGraphicHelper(txtTime, new TimeSpan(3, 0), true);
            timer.setOnTimeFinished(this::finishAttack);
            getGuiHandler().addUpdatable(timer);
        }


        getGuiScene().addLayer(lResource);
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
                            attack.putUnits(menu.getId() - 100, 1, new Point(I, J));
                            menu.setCount((int)attack.getAliveUnits(menu.getId() - 100).filter(soldier -> !soldier.getAttackHelper().isSoldierDeployed()).count());
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

    private void finishAttack()
    {
        getGuiHandler().removeUpdatable(timer);


    }

}
