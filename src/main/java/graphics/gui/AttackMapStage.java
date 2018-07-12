package graphics.gui;

import graphics.GraphicsValues;
import graphics.drawers.Drawer;
import graphics.drawers.drawables.ButtonDrawable;
import graphics.drawers.drawables.ImageDrawable;
import graphics.helpers.*;
import graphics.layers.ResourceLayer;
import graphics.positioning.IsometricPositioningSystem;
import graphics.positioning.NormalPositioningSystem;
import graphics.positioning.PositioningSystem;
import javafx.scene.Group;
import javafx.scene.input.MouseEvent;
import javafx.stage.WindowEvent;
import models.attack.Attack;
import models.attack.attackHelpers.GuardianGiantAttackHelper;
import models.attack.attackHelpers.SingleTargetAttackHelper;
import models.buildings.Building;
import models.buildings.DefensiveTower;
import utils.GraphicsUtilities;
import utils.RectF;

import java.net.URISyntaxException;

public class AttackMapStage extends GUIMapStage
{
    protected Attack theAttack;

    private boolean preview;

    private ResourceLayer lResource;

    public AttackMapStage(Attack attack, double width, double height, boolean preview)
    {
        super(attack.getMap(), width, height);
        this.theAttack = attack;
        this.preview = preview;

        lResource = new ResourceLayer(8,
                new RectF(width - 200 - GraphicsValues.PADDING * 2, 20, 200 + GraphicsValues.PADDING * 2, 70),
                preview ? attack::getTotalResource : attack::getClaimedResource, attack::getTotalResource);
    }

    @Override
    protected void preShow(Group group)
    {
        super.preShow(group);

        if (preview)
        {
            ButtonDrawable btnAttack = new ButtonDrawable("Start Attack", GraphicsValues.IconPaths.Axes, CELL_SIZE, CELL_SIZE);
            btnAttack.setPivot(0, 0);
            btnAttack.setFill(ButtonDrawable.DARK);
            Drawer dBtnAttack = new Drawer(btnAttack);
            dBtnAttack.setPosition(0, getStuffsLayer().getHeight() / ((NormalPositioningSystem)getStuffsLayer().getPosSys()).getScale() - 2);
            dBtnAttack.setLayer(getStuffsLayer());
            dBtnAttack.setClickListener(this::onBtnAttackClick);
        }

        getGuiScene().addLayer(lResource);
    }

    @Override
    public BuildingGraphicHelper addBuilding(Building building)
    {
        building.getAttackHelper().setBuildingIsReal();
        AttackBuildingGraphicHelper graphicHelper;

        if (building instanceof DefensiveTower && !preview)
        {
            if (building.getAttackHelper() instanceof SingleTargetAttackHelper)
            {
                if (building.getAttackHelper() instanceof GuardianGiantAttackHelper)
                    graphicHelper = new GuardianGiantGraphicHelper(building, getObjectsLayer(), map);
                else
                    graphicHelper = new SingleTDefenseGraphicHelper(building, getObjectsLayer(), getMap());
            }
            else
                graphicHelper = new AreaSplashDefenseGraphicHelper(building, getObjectsLayer(), getMap());
        }
        else
            graphicHelper = new AttackBuildingGraphicHelper(building, getObjectsLayer(), getMap());

        if (!preview)
            building.getAttackHelper().setGraphicHelper(graphicHelper);

        setUpBuildingDrawer(graphicHelper.getBuildingDrawer());
        graphicHelper.setUpListeners();
        gHandler.addUpdatable(graphicHelper);

        return graphicHelper;
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
                    drawer.setLayer(lFloor);
                    if (!preview)
                        drawer.setClickListener(this::onMarginalCellClick);
                }
        }
        catch (URISyntaxException e)
        {
            showInfo(e.getMessage());
        }
    }

    protected void onMarginalCellClick(Drawer sender, MouseEvent e)
    {

    }

    private Runnable onAttackStartRequestListener;

    private void callOnAttackStartRequest()
    {
        if (onAttackStartRequestListener != null)
            onAttackStartRequestListener.run();
    }

    public void setOnAttackStartRequestListener(Runnable onAttackStartRequestListener)
    {
        this.onAttackStartRequestListener = onAttackStartRequestListener;
    }

    private void onBtnAttackClick(Drawer sender, MouseEvent e)
    {
        this.close();
        callOnAttackStartRequest();
    }

    @Override
    protected void onClose(WindowEvent event)
    {
        super.onClose(event);
        lResource = null;
    }
}
