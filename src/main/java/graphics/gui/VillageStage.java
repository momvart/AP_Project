package graphics.gui;

import graphics.Fonts;
import graphics.GameLooper;
import graphics.Layer;
import graphics.MenuLayer;
import graphics.MenuLayer.Orientation;
import graphics.drawers.BuildingDrawer;
import graphics.drawers.Drawer;
import graphics.drawers.drawables.RoundRectDrawable;
import graphics.drawers.drawables.TextDrawable;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.paint.Color;
import menus.Menu;
import menus.ParentMenu;
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
        lmenu = new MenuLayer(6, new RectF(0, 0, width, height - CELL_SIZE), Orientation.HORIZONTAL);
        linfo = new Layer(6, new RectF(0, 0, width, height));
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
        lmenu.setItemCellSize(CELL_SIZE);
        lmenu.setCurrentMenu(building.getMenu(new ParentMenu(Menu.Id.VILLAGE_MAIN_MENU, "")));
        lmenu.setClickListener(villageView);
        gScene.addLayer(lmenu);
    }

    public void showRightBar()
    {
        double cellSize = height / 10;
        MenuLayer rightBar = new MenuLayer(5, new RectF(width - cellSize, 0, cellSize, 0), Orientation.VERTICAL);
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
            TextDrawable text = new TextDrawable(split[i], Fonts.getMedium());
            Drawer tdrawer = new Drawer(text);
            tdrawer.setPosition(width / 4, i * LINE_SIZE);
            tdrawer.setLayer(linfo);
        }
        gScene.addLayer(linfo);
    }

}
