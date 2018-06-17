package graphics.helpers;

import graphics.Layer;
import graphics.drawers.BuildingDrawer;
import models.attack.attackHelpers.DefensiveTowerAttackHelper;
import models.buildings.Building;
import models.soldiers.SoldierInjuryReport;
import utils.Point;

import java.net.URISyntaxException;
import java.util.ArrayList;

public class DefensiveTowerGraphicHelper extends GraphicHelper implements IOnDefenseFireListener, IOnDestroyListener
{
    Building building;

    BuildingDrawer buildingDrawer;


    State currentState = State.IDLE;
    IOnReloadListener reloadListener;

    public DefensiveTowerGraphicHelper(Building building, Layer layer)
    {
        this.building = building;
        try
        {
            buildingDrawer = new BuildingDrawer(building);
        }
        catch (URISyntaxException e)
        {
            e.printStackTrace();
        }
        setReloadDuration(1.5);
        buildingDrawer.setLayer(layer);


        buildingDrawer.setPosition(building.getLocation().getX(), building.getLocation().getY());
    }

    private void setUpListeners()
    {
        setReloadListener(building.getAttackHelper());
        DefensiveTowerAttackHelper dtah = (DefensiveTowerAttackHelper)building.getAttackHelper();
        dtah.setFireListener(this);
        dtah.setDestroyListener(this);
    }

    private void makeFire(Point location, DefenseKind defenseKind, ArrayList<SoldierInjuryReport> soldiersInjuredDirectly, ArrayList<SoldierInjuryReport> soldiersInjuredImplicitly)
    {
        currentState = State.FIRING;
        //play fire animation
    }

    private void makeIdle()
    {
        currentState = State.IDLE;
        //show image
    }

    private void makeDestroy()
    {
        currentState = State.DESTROYED;
        //play destroying animation
    }

    @Override
    public void update(double deltaT)
    {
        super.update(deltaT);
        bulletFlyContinue();
    }

    private void bulletFlyContinue()
    {
        if (currentState == State.FIRING)
        {

        }
        //TODO to be decided how to implement
        //if bullet reacher the destination the  status = IDLE
    }

    @Override
    protected void callOnReload()
    {
        if (currentState == State.IDLE)
        {
            super.callOnReload();
        }
    }

    @Override
    public void onDefenseFire(Point targetLocation, DefenseKind defenseKind, ArrayList<SoldierInjuryReport> soldiersInjuredDirectly, ArrayList<SoldierInjuryReport> soldiersInjuredImplicitly)
    {
        makeFire(targetLocation, defenseKind, soldiersInjuredDirectly, soldiersInjuredImplicitly);
    }

    @Override
    public void onDestroy()
    {
        makeDestroy();
    }

    public enum State
    {
        IDLE,
        FIRING,
        DESTROYED;

    }

}
