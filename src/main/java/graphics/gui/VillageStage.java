package graphics.gui;

import com.google.gson.JsonIOException;
import exceptions.ConsoleException;
import exceptions.MyIOException;
import exceptions.MyJsonException;
import graphics.GraphicsValues;
import graphics.drawers.BuildingDrawer;
import graphics.drawers.Drawer;
import graphics.drawers.drawables.ButtonDrawable;
import graphics.gui.dialogs.MapInputDialog;
import graphics.gui.dialogs.SingleChoiceDialog;
import graphics.gui.dialogs.SpinnerDialog;
import graphics.gui.dialogs.TextInputDialog;
import graphics.helpers.BuildingGraphicHelper;
import graphics.helpers.VillageBuildingGraphicHelper;
import graphics.layers.ResourceLayer;
import graphics.positioning.NormalPositioningSystem;
import javafx.scene.Group;
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
import views.dialogs.DialogResultCode;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.*;
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

        ButtonDrawable btnAttack = new ButtonDrawable("Attack", GraphicsValues.IconPaths.Axes, CELL_SIZE, CELL_SIZE);
        btnAttack.setPivot(0, 0);
        btnAttack.setFill(ButtonDrawable.LIGHT);
        Drawer dBtnAttack = new Drawer(btnAttack);
        dBtnAttack.setPosition(0, getStuffsLayer().getHeight() / ((NormalPositioningSystem)getStuffsLayer().getPosSys()).getScale() - 2);
        dBtnAttack.setLayer(getStuffsLayer());
        dBtnAttack.setClickListener(this::onBtnAttackClick);

        ButtonDrawable btnSave = new ButtonDrawable("", GraphicsValues.IconPaths.Save, CELL_SIZE / 2, CELL_SIZE / 2);
        btnSave.setPivot(0, 0);
        btnSave.setFill(Color.rgb(255, 255, 255, 0.6));
        Drawer dBtnSave = new Drawer(btnSave);
        dBtnSave.setPosition(0, 0);
        dBtnSave.setLayer(getStuffsLayer());
        dBtnSave.setClickListener(this::onBtnSaveClick);

        ButtonDrawable btnSettings = new ButtonDrawable("", GraphicsValues.IconPaths.Settings, CELL_SIZE / 2, CELL_SIZE / 2);
        btnSettings.setPivot(0, 0);
        btnSettings.setFill(ButtonDrawable.LIGHT);
        Drawer dBtnSettings = new Drawer(btnSettings);
        dBtnSettings.setPosition(0, 0.5);
        dBtnSettings.setLayer(getStuffsLayer());
        dBtnSettings.setClickListener(this::onBtnSettingsClick);

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

    private void onBtnSaveClick(Drawer sender, MouseEvent event)
    {
        try
        {
            try (BufferedWriter writer = Files.newBufferedWriter(Paths.get("data", "save.json"), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING))
            {
                World.saveGame(writer);
                World.saveSettings();
            }
            catch (JsonIOException ex)
            {
                throw new MyJsonException(ex);
            }
            catch (NoSuchFileException ex)
            {
                throw new MyIOException("File not found.", ex);
            }
            catch (IOException ex)
            {
                throw new MyIOException(ex);
            }
        }
        catch (ConsoleException ex)
        {
            villageView.showError(ex);
        }
    }

    private void onBtnSettingsClick(Drawer sender, MouseEvent event)
    {
        SpinnerDialog spinnerDialog = new SpinnerDialog(getStuffsLayer(), width, height, "Set game speed", 1, 3, 0.5);
        DialogResult dialogResult = spinnerDialog.showDialog();

        if (dialogResult.getResultCode().equals(DialogResultCode.YES))
            World.sSettings.setGameSpeed((double)(dialogResult.getData("speed")));

    }
}
