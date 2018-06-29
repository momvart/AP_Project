package graphics.gui;

import graphics.*;
import graphics.drawers.BuildingDrawer;
import graphics.drawers.Drawer;
import graphics.drawers.drawables.RoundRectDrawable;
import graphics.drawers.drawables.TextDrawable;
import graphics.gui.dialogs.MapInputDialog;
import graphics.gui.dialogs.NumberInputDialog;
import graphics.gui.dialogs.SingleChoiceDialog;
import graphics.helpers.BuildingGraphicHelper;
import graphics.helpers.VillageBuildingGraphicHelper;
import graphics.layers.MenuLayer;
import graphics.layers.ResourceLayer;
import graphics.layers.ToastLayer;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import menus.Menu;
import menus.ParentMenu;
import models.Map;
import models.Village;
import models.buildings.Building;
import utils.RectF;
import views.VillageView;
import views.dialogs.DialogResult;

public class VillageStage extends MapStage
{
    private Canvas guiCanvas;
    private GameLooper guiLooper;
    private GameScene guiScene;
    private GraphicHandler guiHandler;
    private Village village;

    private MenuLayer lmenu;
    private ResourceLayer lresource;
    private ToastLayer linfo;

    private VillageView villageView;
    private final double CELL_SIZE = height / 10;
    private final double LINE_SIZE = height / 20;
    private final double CHARACTER_SPACING = width / 100;

    public VillageStage(Village village, double width, double height)
    {
        super(village.getMap(), width, height);
        lmenu = new MenuLayer(6, new RectF(0, height - CELL_SIZE - GraphicsValues.PADDING, width, CELL_SIZE), MenuLayer.Orientation.HORIZONTAL);
        lresource = new ResourceLayer(8, new RectF(width - 200 - GraphicsValues.PADDING * 2, 20, 200 + GraphicsValues.PADDING * 2, 70), village);
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


        lmenu.setItemCellSize(CELL_SIZE);
        lmenu.setClickListener(item ->
        {
            villageView.setCurrentMenu(lmenu.getCurrentMenu(), true);
            villageView.onItemClicked(item);
        });

        linfo = new ToastLayer(7, new RectF(0, 0, width, height), guiHandler);

        guiScene.addLayer(lmenu);
        guiScene.addLayer(linfo);
        guiScene.addLayer(lresource);

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
        lmenu.setCurrentMenu(building.getMenu(new ParentMenu(Menu.Id.VILLAGE_MAIN_MENU, "")));
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


    public DialogResult showSingleChoiceDialog(String message)
    {
        SingleChoiceDialog dialog = new SingleChoiceDialog(linfo, width, height, message);
        return dialog.showDialog();
    }

    public DialogResult showNumberInputDialog(String message, int count)
    {
        NumberInputDialog dialog = new NumberInputDialog(linfo, width, height, message, count);
        return dialog.showDialog();
    }

    public DialogResult showMapInputDialog(String message, Map map)
    {
        MapInputDialog dialog = new MapInputDialog(linfo, width, height, message, map);
        return dialog.showDialog();
    }


    @Override
    protected void onClickAnywhereElse(MouseEvent event)
    {
        super.onClickAnywhereElse(event);
        lmenu.setCurrentMenu(null);
    }
}
