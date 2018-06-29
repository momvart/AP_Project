package graphics.gui;

import exceptions.ConsoleException;
import graphics.*;
import graphics.drawers.Drawer;
import graphics.drawers.drawables.ImageDrawable;
import graphics.drawers.drawables.RoundRectDrawable;
import graphics.drawers.drawables.TextDrawable;
import graphics.helpers.*;
import graphics.layers.MenuLayer;
import graphics.layers.ToastLayer;
import graphics.positioning.IsometricPositioningSystem;
import graphics.positioning.PositioningSystem;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.paint.Color;
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

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Optional;

public class AttackStage extends MapStage
{
    private Attack attack;
    private MenuLayer lmenu;
    private ToastLayer linfo;
    private Canvas guiCanvas;
    private GameLooper guiLooper;
    private GameScene guiScene;
    private GraphicHandler guiHandler;

    private final double CELL_SIZE = height / 10;
    private final double LINE_SIZE = height / 20;
    private final double CHARACTER_SPACING = width / 100;
    private final String NO_SOLDIER_FOCUSED_ERROR = "No soldier focused yet";
    private final String SOLDIER_ICON_PATH = "assets/soldiers/";

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
            if (attack.getAllUnits(i).size() == 0)
            {
                submenu.setFocusable(false);
                submenu.setIconPath(SOLDIER_ICON_PATH + SoldierValues.getSoldierInfo(i).getName().toLowerCase().replaceAll(" ", "") + "/IconG.png");
            }
            else
            {
                submenu.setFocusable(true);
                submenu.setIconPath(SOLDIER_ICON_PATH + SoldierValues.getSoldierInfo(i).getName().toLowerCase().replaceAll(" ", "") + "/Icon.png");
            }
            soldierMenuItems.add(submenu);
        }
        soldierMenuItems.forEach(parentMenu::insertItem);
        lmenu.setClickListener(item ->
        {
            soldierMenuItems.forEach(soldierMenuItem -> soldierMenuItem.setFocused(false));
            item.setFocused(true);
        });
        lmenu.setCurrentMenu(parentMenu);

        linfo = new ToastLayer(7, new RectF(0, 0, width, height), gHandler);

        guiScene.addLayer(lmenu);
        guiScene.addLayer(linfo);

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
                        Optional<Menu> menu = lmenu.getCurrentMenu().getMenuItems().stream().filter(Menu::isFocused).findFirst();
                        if (menu.isPresent())
                        {
                            try
                            {
                                attack.putUnits(menu.get().getId() - 100, 1, new Point(I, J));
                            }
                            catch (ConsoleException e)
                            {
                                e.printStackTrace();
                            }
                        }
                        else
                            showInfo(NO_SOLDIER_FOCUSED_ERROR);
                    });
                    drawer.setLayer(lFloor);
                }
        }
        catch (URISyntaxException e)
        {
            showInfo(e.getMessage());
        }
    }

    public void showInfo(String info)
    {
        linfo.removeAllObjects();
        info = "\n" + info;
        String[] split = info.split("\n");
        int max = 0;
        for (String aSplit : split) max = aSplit.length() > max ? aSplit.length() : max;
        RoundRectDrawable bg = new RoundRectDrawable(CHARACTER_SPACING * max, (split.length) * LINE_SIZE, 10, Color.rgb(0, 0, 0, 0.6));
        Drawer drawer = new Drawer(bg);
        drawer.setPosition(width / 2 - bg.getWidth() / 2, -LINE_SIZE / 2);
        drawer.setLayer(linfo);
        for (int i = 0; i < split.length; i++)
        {
            TextDrawable text = new TextDrawable(split[i], Color.WHITE, Fonts.getMedium());
            Drawer tdrawer = new Drawer(text);
            tdrawer.setPosition(width / 2 - bg.getWidth() / 2, (i) * LINE_SIZE - LINE_SIZE / 2);
            tdrawer.setLayer(linfo);
        }

        linfo.show(guiHandler);
    }

}
