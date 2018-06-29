package graphics.gui;

import graphics.*;
import graphics.drawers.BuildingDrawer;
import graphics.drawers.Drawer;
import graphics.drawers.drawables.ButtonDrawable;
import graphics.drawers.drawables.RoundRectDrawable;
import graphics.drawers.drawables.TextDrawable;
import graphics.gui.dialogs.MapInputDialog;
import graphics.gui.dialogs.NumberInputDialog;
import graphics.gui.dialogs.SingleChoiceDialog;
import graphics.gui.dialogs.TextInputDialog;
import graphics.helpers.BuildingGraphicHelper;
import graphics.helpers.VillageBuildingGraphicHelper;
import graphics.layers.Layer;
import graphics.layers.MenuLayer;
import graphics.layers.ResourceLayer;
import graphics.layers.ToastLayer;
import graphics.positioning.NormalPositioningSystem;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.ButtonType;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import menus.AttackMapItem;
import menus.Menu;
import menus.ParentMenu;
import models.Map;
import models.Village;
import models.World;
import models.buildings.Building;
import utils.RectF;
import views.VillageView;
import views.dialogs.DialogResult;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class VillageStage extends MapStage
{
    private Canvas guiCanvas;
    private GameLooper guiLooper;
    private GameScene guiScene;
    private GraphicHandler guiHandler;
    private Village village;

    private MenuLayer lMenu;
    private ResourceLayer lResource;
    private ToastLayer lInfo;
    private Layer lStuffs;

    private VillageView villageView;
    private final double CELL_SIZE = height / 10;
    private final double LINE_SIZE = height / 20;
    private final double CHARACTER_SPACING = width / 100;

    public VillageStage(Village village, double width, double height)
    {
        super(village.getMap(), width, height);
        lMenu = new MenuLayer(6, new RectF(0, height - CELL_SIZE - GraphicsValues.PADDING * 2, width, CELL_SIZE + 2 * GraphicsValues.PADDING), MenuLayer.Orientation.HORIZONTAL);
        lMenu.setItemCellSize(CELL_SIZE);
        lResource = new ResourceLayer(8, new RectF(width - 200 - GraphicsValues.PADDING * 2, 20, 200 + GraphicsValues.PADDING * 2, 70), village);
        lStuffs = new Layer(10, new RectF(GraphicsValues.PADDING, GraphicsValues.PADDING, width - 2 * GraphicsValues.PADDING, height - 2 * GraphicsValues.PADDING));
        lStuffs.setPosSys(new NormalPositioningSystem(CELL_SIZE + 2 * GraphicsValues.PADDING));
    }

    public void setVillageView(VillageView villageView)
    {
        this.villageView = villageView;
    }

    @Override
    protected void preShow(Group group)
    {
        super.preShow(group);

        guiCanvas = new Canvas(width * GraphicsValues.getScale(), height * GraphicsValues.getScale());
        group.getChildren().add(guiCanvas);

        guiHandler = new GraphicHandler(guiCanvas.getGraphicsContext2D(), new RectF(0, 0, guiCanvas.getWidth(), guiCanvas.getHeight()));
        guiScene = new GameScene(width, height);


        lMenu.setItemCellSize(CELL_SIZE);
        lMenu.setClickListener(item ->
        {
            villageView.setCurrentMenu(lMenu.getCurrentMenu(), true);
            villageView.onItemClicked(item);
        });

        lInfo = new ToastLayer(7, new RectF(0, 0, width, height), guiHandler);

        ButtonDrawable btnAttack = new ButtonDrawable("Attack", GraphicsValues.UI_ASSETS_PATH + "/axes.png", CELL_SIZE, CELL_SIZE);
        btnAttack.setPivot(0, 0);
        btnAttack.setFill(Color.rgb(255, 255, 255, 0.6));
        Drawer dBtnAttack = new Drawer(btnAttack);
        dBtnAttack.setPosition(0, lStuffs.getHeight() / ((NormalPositioningSystem)lStuffs.getPosSys()).getScale() - 2);
        dBtnAttack.setLayer(lStuffs);
        dBtnAttack.setClickListener(this::onBtnAttackClick);


        guiScene.addLayer(lMenu);
        guiScene.addLayer(lInfo);
        guiScene.addLayer(lResource);
        guiScene.addLayer(lStuffs);

        guiHandler.setScene(guiScene);

        new GameLooper(guiHandler).start();

        graphicHandlers.add(guiHandler);
    }

    @Override
    public BuildingGraphicHelper addBuilding(Building building)
    {
        VillageBuildingGraphicHelper graphicHelper = new VillageBuildingGraphicHelper(building, getObjectsLayer(), getMap());

        building.createAndSetVillageHelper();

        setUpBuildingDrawer(graphicHelper.getBuildingDrawer());

        building.getVillageHelper().setGraphicHelper(graphicHelper);

        gHandler.addUpdatable(graphicHelper);

        return graphicHelper;
    }

    @Override
    protected void setUpBuildingDrawer(BuildingDrawer drawer)
    {
        super.setUpBuildingDrawer(drawer);
        drawer.setClickListener((sender, event) ->
        {
            showBottomMenu(drawer.getBuilding());
        });
    }

    public void showBottomMenu(Building building)
    {
        lMenu.setCurrentMenu(building.getMenu(new ParentMenu(Menu.Id.VILLAGE_MAIN_MENU, "")));
    }

    public void showInfo(String info)
    {
        lInfo.removeAllObjects();
        info = "\n" + info;
        String[] split = info.split("\n");
        int max = 0;
        for (String aSplit : split) max = aSplit.length() > max ? aSplit.length() : max;
        RoundRectDrawable bg = new RoundRectDrawable(CHARACTER_SPACING * max, (split.length) * LINE_SIZE, 10, Color.rgb(0, 0, 0, 0.6));
        Drawer drawer = new Drawer(bg);
        drawer.setPosition(width / 2 - bg.getWidth() / 2, -LINE_SIZE / 2);
        drawer.setLayer(lInfo);
        for (int i = 0; i < split.length; i++)
        {
            TextDrawable text = new TextDrawable(split[i], Color.WHITE, Fonts.getMedium());
            Drawer tdrawer = new Drawer(text);
            tdrawer.setPosition(width / 2 - bg.getWidth() / 2, (i) * LINE_SIZE - LINE_SIZE / 2);
            tdrawer.setLayer(lInfo);
        }

        lInfo.show(guiHandler);
    }


    public DialogResult showSingleChoiceDialog(String message)
    {
        SingleChoiceDialog dialog = new SingleChoiceDialog(lInfo, width, height, message);
        return dialog.showDialog();
    }

    public DialogResult showSingleChoiceDialog(String message, ButtonType yes, ButtonType no)
    {
        SingleChoiceDialog dialog = new SingleChoiceDialog(lInfo, width, height, message, yes, no);
        return dialog.showDialog();
    }

    public DialogResult showNumberInputDialog(String message, int count)
    {
        NumberInputDialog dialog = new NumberInputDialog(lInfo, width, height, message, count);
        return dialog.showDialog();
    }

    public DialogResult showMapInputDialog(String message, Map map)
    {
        MapInputDialog dialog = new MapInputDialog(lInfo, width, height, message, map);
        return dialog.showDialog();
    }

    public DialogResult showOpenMapDialog(String message)
    {
        TextInputDialog dialog = new TextInputDialog(lInfo, width, height, message);
        return dialog.showDialog();
    }


    @Override
    protected void onClickAnywhereElse(MouseEvent event)
    {
        super.onClickAnywhereElse(event);
        lMenu.setCurrentMenu(null);
    }


    public void onBtnAttackClick(Drawer sender, MouseEvent event)
    {
        List<Path> paths = World.sSettings.getAttackMapPaths().stream().map(Paths::get).collect(Collectors.toList());
        ParentMenu mainMenu = new ParentMenu(Menu.Id.ATTACK_MAIN_MENU, "");
        mainMenu.insertItem(new Menu(Menu.Id.ATTACK_LOAD_MAP, "Load Map", GraphicsValues.IconPaths.NewMap));
        paths.forEach(p -> mainMenu.insertItem(new AttackMapItem(mainMenu, p)));
        lMenu.setCurrentMenu(mainMenu);
    }
}
