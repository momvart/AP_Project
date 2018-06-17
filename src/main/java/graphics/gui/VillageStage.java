package graphics.gui;

import graphics.*;
import graphics.drawers.*;
import graphics.drawers.drawables.*;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import menus.*;
import models.Map;
import models.buildings.Building;
import utils.RectF;
import views.VillageView;

public class VillageStage extends MapStage
{
    private Canvas guiCanvas;
    private GameLooper guiLooper;
    private GameScene guiScene;

    private MenuLayer lmenu;
    private Layer linfo;

    private VillageView villageView;
    private final double CELL_SIZE = height / 10;
    private final double LINE_SIZE = height / 20;

    public VillageStage(Map map, double width, double height)
    {
        super(map, width, height);
        lmenu = new MenuLayer(6, new RectF(0, height - CELL_SIZE, width, CELL_SIZE), MenuLayer.Orientation.HORIZONTAL);
        linfo = new Layer(7, new RectF(0, 0, width, height));
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

        GraphicHandler guiHandler = new GraphicHandler(guiCanvas.getGraphicsContext2D(), new RectF(0, 0, guiCanvas.getWidth(), guiCanvas.getHeight()));
        guiCanvas.setOnMouseClicked(guiHandler::handleMouseClick);
        guiScene = new GameScene(width, height);

        showRightBar();

        lmenu.setItemCellSize(CELL_SIZE);
        lmenu.setClickListener(item ->
        {
            villageView.setCurrentMenu(lmenu.getCurrentMenu(), true);
            villageView.onItemClicked(item);
        });

        guiScene.addLayer(lmenu);
        guiScene.addLayer(linfo);

        guiHandler.setScene(guiScene);

        new GameLooper(guiHandler).start();

        graphicHandlers.add(guiHandler);
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

    public void showRightBar()
    {
        double cellSize = height / 10;
        MenuLayer rightBar = new MenuLayer(5, new RectF(width - cellSize, 0, cellSize, 0), MenuLayer.Orientation.VERTICAL);
        rightBar.setItemCellSize(cellSize);

        gScene.addLayer(rightBar);
    }

    public void showInfo(String info)
    {
        linfo.removeAllObjects();
        String[] split = info.split("\n");
        RoundRectDrawable bg = new RoundRectDrawable(width / 4, (split.length + 1) * LINE_SIZE, 10, Color.rgb(0, 0, 0, 0.6));
        Drawer drawer = new Drawer(bg);
        drawer.setLayer(linfo);
        for (int i = 0; i < split.length; i++)
        {
            TextDrawable text = new TextDrawable(split[i], Color.WHITE, Fonts.getMedium());
            Drawer tdrawer = new Drawer(text);
            tdrawer.setPosition(0, i * LINE_SIZE);
            tdrawer.setLayer(linfo);
        }
    }

}
