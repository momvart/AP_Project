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

public class VillageStage extends GUIMapStage
{
    private Village village;

    private ResourceLayer lResource;

    private VillageView villageView;

    public VillageStage(Village village, double width, double height)
    {
        super(village.getMap(), width, height);
        lResource = new ResourceLayer(8,
                new RectF(width - 200 - GraphicsValues.PADDING * 2, 20, 200 + GraphicsValues.PADDING * 2, 70),
                village::getResources, village::getTotalResourceCapacity);
    }

    public void setVillageView(VillageView villageView)
    {
        this.villageView = villageView;
    }

    @Override
    protected void preShow(Group group)
    {
        super.preShow(group);

        getMenuLayer().setClickListener(item ->
        {
            villageView.setCurrentMenu(getMenuLayer().getCurrentMenu(), true);
            villageView.onItemClicked(item);
        });

        ButtonDrawable btnAttack = new ButtonDrawable("Attack", GraphicsValues.UI_ASSETS_PATH + "/axes.png", CELL_SIZE, CELL_SIZE);
        btnAttack.setPivot(0, 0);
        btnAttack.setFill(Color.rgb(255, 255, 255, 0.6));
        Drawer dBtnAttack = new Drawer(btnAttack);
        dBtnAttack.setPosition(0, getStuffsLayer().getHeight() / ((NormalPositioningSystem)getStuffsLayer().getPosSys()).getScale() - 2);
        dBtnAttack.setLayer(getStuffsLayer());
        dBtnAttack.setClickListener(this::onBtnAttackClick);

        getGuiScene().addLayer(lResource);
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

    private void showBottomMenu(Building building)
    {
        getMenuLayer().setCurrentMenu(building.getMenu(new ParentMenu(Menu.Id.VILLAGE_MAIN_MENU, "")));
    }


    public DialogResult showSingleChoiceDialog(String message, ButtonType yes, ButtonType no)
    {
        SingleChoiceDialog dialog = new SingleChoiceDialog(getInfoLayer(), width, height, message, yes, no);
        return dialog.showDialog();
    }

    public DialogResult showMapInputDialog(String message, Map map)
    {
        MapInputDialog dialog = new MapInputDialog(getStuffsLayer(), width, height, message, map);
        return dialog.showDialog();
    }

    public DialogResult showOpenMapDialog(String message)
    {
        TextInputDialog dialog = new TextInputDialog(getStuffsLayer(), width, height, message);
        return dialog.showDialog();
    }

    @Override
    protected void onClickAnywhereElse(MouseEvent event)
    {
        super.onClickAnywhereElse(event);
        getMenuLayer().setCurrentMenu(null);
    }

    public void onBtnAttackClick(Drawer sender, MouseEvent event)
    {
        List<Path> paths = World.sSettings.getAttackMapPaths().stream().map(Paths::get).collect(Collectors.toList());
        ParentMenu mainMenu = new ParentMenu(Menu.Id.ATTACK_MAIN_MENU, "");
        mainMenu.insertItem(new Menu(Menu.Id.ATTACK_LOAD_MAP, "Load Map", GraphicsValues.IconPaths.NewMap));
        paths.forEach(p -> mainMenu.insertItem(new AttackMapItem(mainMenu, p)));
        getMenuLayer().setCurrentMenu(mainMenu);
    }
}
