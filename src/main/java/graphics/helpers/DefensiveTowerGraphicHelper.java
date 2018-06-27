package graphics.helpers;

import graphics.layers.Layer;
import models.Map;
import models.attack.attackHelpers.IOnBulletHitListener;
import models.buildings.Building;
import models.soldiers.SoldierInjuryReport;
import utils.Point;
import utils.PointF;

import java.util.ArrayList;

public abstract class DefensiveTowerGraphicHelper extends AttackBuildingGraphicHelper implements IOnDefenseFireListener
{
    IOnBulletHitListener bulletHitListener;
    State currentState = State.IDLE;
    protected PointF bulletUltimatePosition;
    protected final int bulletMaximumSpeed = 1;

    public DefensiveTowerGraphicHelper(Building building, Layer layer, Map map)
    {
        super(building, layer, map);
    }

    public IOnBulletHitListener getBulletHitListener()
    {
        return bulletHitListener;
    }

    public void setBulletHitListener(IOnBulletHitListener bulletHitListener)
    {
        this.bulletHitListener = bulletHitListener;
    }

    @Override
    public void setUpListeners()
    {
        super.setUpListeners();
    }

    private void splashAreaIfNeeded(Point targetLocation, DefenseKind defenseKind)
    {
        if (defenseKind == DefenseKind.AREA_SPLASH)
            playSplashAnimation(targetLocation);
    }

    private void playSplashAnimation(Point targetLocation)
    {
        //TODO playing an splashing animation
    }

    @Override
    public void makeDestroy()
    {
        super.makeDestroy();
        currentState = State.DESTROYED;
    }

    private void makeIdle()
    {
        currentState = State.IDLE;
    }


    @Override
    public void update(double deltaT)
    {
        super.update(deltaT);
    }

    @Override
    protected void callOnReload()
    {
        if (currentState == State.IDLE)
        {
            currentState = State.FIRING;
            super.callOnReload();
        }
    }

    public PointF getBulletUltimatePosition()
    {
        return bulletUltimatePosition;
    }

    public void setBulletUltimatePosition(PointF bulletUltimatePosition)
    {
        this.bulletUltimatePosition.setX(bulletUltimatePosition.getX());
        this.bulletUltimatePosition.setY(bulletUltimatePosition.getY());
    }

    @Override
    public void onDefenseFire(Point targetLocation, DefenseKind defenseKind, ArrayList<SoldierInjuryReport> soldiersInjuredDirectly, ArrayList<SoldierInjuryReport> soldiersInjuredImplicitly)
    {
        splashAreaIfNeeded(targetLocation, defenseKind);
        currentState = State.IDLE;
    }

    public enum State
    {
        IDLE,
        FIRING,
        DESTROYED;
    }
}
