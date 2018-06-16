package graphics.gui;

import graphics.GameLooper;
import graphics.MenuLayer;
import graphics.MenuLayer.Orientation;
import graphics.drawers.BuildingDrawer;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import menus.Menu;
import menus.ParentMenu;
import models.Map;
import models.buildings.Building;
import utils.RectF;

public class VillageStage extends MapStage
{
    private Canvas canvas;
    private GameLooper looper;

    public VillageStage(Map map, double width, double height)
    {
        super(map, width, height);

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
        double cellSize = height / 10;
        MenuLayer menu = new MenuLayer(6, new RectF(0, 0, width, height - cellSize), Orientation.HORIZONTAL);
        menu.setItemCellSize(cellSize);
        menu.setCurrentMenu(building.getMenu(new ParentMenu(Menu.Id.VILLAGE_MAIN_MENU, "")));
        menu.getCurrentMenu().getItems().forEach(System.out::println);
        gScene.addLayer(menu);
    }


    public void showRightBar()
    {
        double cellSize = height / 10;
        MenuLayer rightBar = new MenuLayer(5, new RectF(width - cellSize, 0, cellSize, 0), Orientation.VERTICAL);
        rightBar.setItemCellSize(cellSize);

        gScene.addLayer(rightBar);
    }


}
