package graphics.helpers;

import graphics.Layer;
import graphics.drawers.Drawer;
import models.Map;
import models.attack.attackHelpers.DefensiveTowerAttackHelper;
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
    Drawer bulletDrawer;
    protected boolean hasBulletHitTarget = false;
    protected PointF bulletUltimatePosition;

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

    protected void bulletFlyContinue()
    {//implemented in subClasses
    }

    @Override
    public void update(double deltaT)
    {
        super.update(deltaT);
        bulletFlyContinue();
    }

    @Override
    protected void callOnReload()
    {
        if (currentState == State.IDLE)
        {
            hasBulletHitTarget = false;
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
        this.bulletUltimatePosition = bulletUltimatePosition;
    }

    @Override
    public void onDefenseFire(Point targetLocation, DefenseKind defenseKind, ArrayList<SoldierInjuryReport> soldiersInjuredDirectly, ArrayList<SoldierInjuryReport> soldiersInjuredImplicitly)
    {
        splashAreaIfNeeded(targetLocation, defenseKind);
        for (SoldierInjuryReport report : soldiersInjuredDirectly)
        {
            getBuildingDrawer().healthDecreseBarLoading(report.getInitialHealth(), report.getFinalHealth());
        }
        for (SoldierInjuryReport report : soldiersInjuredImplicitly)
        {
            getBuildingDrawer().healthDecreseBarLoading(report.getInitialHealth(), report.getFinalHealth());
        }
        currentState = State.IDLE;
    }

    public enum State
    {
        IDLE,
        FIRING,
        DESTROYED;
    }
}
