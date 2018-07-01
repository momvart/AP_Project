package graphics.helpers;

import graphics.layers.Layer;
import models.Map;
import models.attack.attackHelpers.DefensiveTowerAttackHelper;
import models.attack.attackHelpers.IOnBulletHitListener;
import models.buildings.Building;
import models.soldiers.Soldier;
import utils.PointF;

public abstract class DefensiveTowerGraphicHelper extends AttackBuildingGraphicHelper implements IOnBulletTriggerListener
{
    DefensiveTowerAttackHelper attackHelper;
    IOnBulletHitListener bulletHitListener;
    State currentState = State.IDLE;
    protected PointF bulletUltimatePosition;
    Layer layer;


    public DefensiveTowerGraphicHelper(Building building, Layer layer, Map map)
    {
        super(building, layer, map);
        setReloadDuration(0.5);
        attackHelper = (DefensiveTowerAttackHelper)building.getAttackHelper();
        this.layer = layer;
        buildingDrawer.updateDrawer();
    }

    public Layer getLayer()
    {
        return layer;
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
        attackHelper.setTriggerListener(this);
        this.bulletHitListener = attackHelper;
        this.setReloadListener(attackHelper);
    }

    private void splashAreaIfNeeded(PointF targetLocation, DefenseKind defenseKind)
    {
        if (defenseKind == DefenseKind.AREA_SPLASH)
            playSplashAnimation(targetLocation);
    }

    private void playSplashAnimation(PointF targetLocation)
    {
        //TODO playing an splashing animation
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
    public void callOnReload()
    {
        super.callOnReload();
        if (currentState == State.IDLE)
        {
            currentState = State.FIRING;
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
    public void onBulletTrigger(PointF targetedPoint, Soldier soldier)
    {
        bulletUltimatePosition = targetedPoint;
        triggerBullet(soldier);
    }

    protected abstract void triggerBullet(Soldier soldier);

    public void onBulletHit(DefenseKind defenseKind)
    {
        bulletHitListener.onBulletHit();
        splashAreaIfNeeded(bulletUltimatePosition, defenseKind);
        currentState = State.IDLE;
    }

    public abstract BulletHelper getBullet();

    public enum State
    {
        IDLE,
        FIRING,
        DESTROYED;
    }
}