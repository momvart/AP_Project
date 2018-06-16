package graphics.gui;

import graphics.*;
import graphics.drawers.*;
import graphics.drawers.drawables.*;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.paint.Color;
import menus.*;
import models.Map;
import models.buildings.Building;
import utils.RectF;
import views.VillageView;

public class VillageStage extends MapStage
{
    private Canvas canvas;
    private GameLooper looper;
    private MenuLayer lmenu;
    private Layer linfo;
    private VillageView villageView;
    private final double CELL_SIZE = height / 10;
    private final double LINE_SIZE = height / 20;

    public VillageStage(Map map, double width, double height)
    {
        super(map, width, height);
        lmenu = new MenuLayer(6, new RectF(0, 0, width, height - CELL_SIZE), MenuLayer.Orientation.HORIZONTAL);
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
        canvas = new Canvas(getWidth(), getHeight());
        canvas.setOnMouseClicked(gHandler::handleMouseClick);
        group.getChildren().add(canvas);
        showRightBar();


        lmenu.setItemCellSize(CELL_SIZE);
        lmenu.setClickListener(item ->
        {
            villageView.setCurrentMenu(lmenu.getCurrentMenu(), true);
            villageView.onItemClicked(item);
        });

        gScene.addLayer(lmenu);
        gScene.addLayer(linfo);
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
