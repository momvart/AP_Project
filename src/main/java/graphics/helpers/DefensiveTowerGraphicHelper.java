package graphics.helpers;

import graphics.Layer;
import graphics.drawers.Drawer;
import models.attack.attackHelpers.DefensiveTowerAttackHelper;
import models.attack.attackHelpers.IOnBulletHitListener;
import models.buildings.Building;
import models.soldiers.SoldierInjuryReport;
import utils.Point;
import utils.PointF;

import java.util.ArrayList;

public class DefensiveTowerGraphicHelper extends BuildingGraphicHelper implements IOnDefenseFireListener
{
    IOnReloadListener reloadListener;
    IOnBulletHitListener bulletHitListener;
    DefensiveTowerAttackHelper attackHelper;
    State currentState = State.IDLE;
    Drawer bulletDrawer;
    private boolean hasBulletHitTarget = false;
    private PointF bulletUltimatePosition;

    public DefensiveTowerGraphicHelper(Building building, Layer layer)
    {
        super(building, layer);
        attackHelper = (DefensiveTowerAttackHelper)building.getAttackHelper();
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
        setBulletHitListener(attackHelper);
        setReloadListener(attackHelper);
        attackHelper.setDefenseFireListener(this);
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

    private void bulletFlyContinue()
    {
        if (currentState == State.FIRING)
        {
            if (bulletUltimatePosition != null)
            {
                if (PointF.euclideanDistance(bulletDrawer.getPosition(), bulletUltimatePosition) < 0.01)
                {
                    hasBulletHitTarget = true;
                    bulletHitListener.onBulletHit();
                    return;
                }
                //logic of bullet flying
            }
        }
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

    @Override
    public void onDestroy()
    {
        makeDestroy();
    }



}
