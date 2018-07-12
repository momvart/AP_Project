package graphics.helpers;

import graphics.IFrameUpdatable;
import graphics.drawers.Drawer;
import graphics.drawers.drawables.ImageDrawable;
import graphics.layers.Layer;
import models.attack.attackHelpers.NetworkHelper;
import models.soldiers.Soldier;
import utils.GraphicsUtilities;
import utils.PointF;

import java.net.URISyntaxException;

public class BulletHelper implements IFrameUpdatable
{
    public boolean inProgress = false;
    protected DefensiveTowerGraphicHelper towerGraphicHelper;
    protected final double maxSpeed = 4;//TODO‌ to be manipulated
    protected Drawer drawer;
    protected PointF start;
    protected PointF end;
    protected double speed = maxSpeed;
    protected boolean hitTarget = false;
    protected double cos;
    protected double sin;
    protected boolean isReal;

    public BulletHelper(DefensiveTowerGraphicHelper towerGraphicHelper, Layer layer)
    {
        this.towerGraphicHelper = towerGraphicHelper;
        isReal = towerGraphicHelper.getAttackHelper().isReal();
        try
        {
            ImageDrawable imageDrawable = GraphicsUtilities.createImageDrawable("assets/bullets/arrow.png", 10, 10, true, 0.5, 0.5);
            drawer = new Drawer(imageDrawable);
            drawer.addUpdatable(this);
        }
        catch (URISyntaxException e) {}
        setUpBulletProperties(layer);
    }

    public void setUpBulletProperties(Layer layer)
    {
        if (towerGraphicHelper.getBuildingDrawer() != null)
        {
            drawer.setPosition(towerGraphicHelper.getBuildingDrawer().getPosition().getX(), towerGraphicHelper.getBuildingDrawer().getPosition().getY());
            drawer.setLayer(layer);
        }
    }

    Soldier targetSoldier;

    public void startNewWave(final PointF start, final PointF end, Soldier soldier, boolean networkPermission)
    {
        if (!isReal && !networkPermission)
        {
            onMoveFinish();
            return;
        }
        this.start = start;
        this.end = end;
        targetSoldier = soldier;
        if (soldier != null)
            setUpCosSin(start, end);
        hitTarget = false;
        inProgress = true;
        if (isReal)
            NetworkHelper.bulletStartNewWave(towerGraphicHelper.getAttackHelper().getBuilding().getId(), start, end, soldier);
    }

    private void setUpCosSin(PointF start, PointF end)
    {
        double distance = PointF.euclideanDistance(start, end);
        cos = (end.getX() - start.getX()) / distance;
        sin = (end.getY() - start.getY()) / distance;
    }

    @Override
    public void update(double deltaT)
    {
        doReplacing(deltaT);
    }

    public void doReplacing(double deltaT)
    {
        if (end == null || start == null)
            return;
        if (hitTarget || !inProgress)
            return;
        if (drawer == null || targetSoldier == null || targetSoldier.getAttackHelper() == null
                || targetSoldier.getAttackHelper().getGraphicHelper() == null
                || targetSoldier.getAttackHelper().getGraphicHelper().getDrawer() == null
                || targetSoldier.getAttackHelper().getGraphicHelper().getDrawer().getPosition() == null
                || targetSoldier.getAttackHelper().isDead()
                || PointF.euclideanDistance(drawer.getPosition(), targetSoldier.getAttackHelper().getGraphicHelper().getDrawer().getPosition()
        ) < .3)
        {
            onMoveFinish();
            return;
        }
        double step = deltaT * speed;
        drawer.getDrawable().setRotation(getAngle(sin, cos));
        setUpCosSin(drawer.getPosition(), targetSoldier.getAttackHelper().getGraphicHelper().getDrawer().getPosition());
        drawer.setPosition(drawer.getPosition().getX() + step * cos, drawer.getPosition().getY() + step * sin);
    }

    public void onMoveFinish()
    {
        hitTarget = true;
        drawer.setPosition(towerGraphicHelper.getBuildingDrawer().getPosition().getX(), towerGraphicHelper.getBuildingDrawer().getPosition().getY());
        inProgress = false;
        if (!targetSoldier.getAttackHelper().isDead())
            towerGraphicHelper.onBulletHit(DefenseKind.SINGLE_TARGET);
        start = null;
        end = null;
        targetSoldier = null;
        cos = -1;
        sin = -1;
    }

    private double getAngle(double sin, double cos)
    {
        return cos > 0 ? Math.asin(sin) : sin > 0 ? (Math.PI - Math.asin(sin)) : (-Math.PI - Math.asin(sin));
    }

    @Override
    public String toString()
    {
        return "BulletHelper{" +
                "inProgress=" + inProgress +
                ", towerGraphicHelper=" + towerGraphicHelper +
                ", maxSpeed=" + maxSpeed +
                ", drawer=" + drawer +
                ", start=" + start +
                ", end=" + end +
                ", speed=" + speed +
                ", hitTarget=" + hitTarget +
                ", cos=" + cos +
                ", sin=" + sin +
                ", targetSoldier=" + targetSoldier +
                '}';
    }
}