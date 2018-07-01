package graphics.helpers;

import graphics.IFrameUpdatable;
import graphics.drawers.Drawer;
import graphics.drawers.drawables.ImageDrawable;
import graphics.layers.Layer;
import models.soldiers.Soldier;
import utils.GraphicsUtilities;
import utils.PointF;

import java.net.URISyntaxException;

public class BulletHelper implements IFrameUpdatable
{
    public boolean inProgress = false;
    protected DefensiveTowerGraphicHelper towerGraphicHelper;
    protected final double maxSpeed = 2;//TODO‌ to be manipulated
    protected Drawer drawer;
    protected PointF start;
    protected PointF end;
    protected double speed = maxSpeed;
    protected boolean hitTarget = false;
    protected double cos;
    protected double sin;

    public BulletHelper(DefensiveTowerGraphicHelper towerGraphicHelper, Layer layer)
    {
        this.towerGraphicHelper = towerGraphicHelper;

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
        drawer.setPosition(towerGraphicHelper.getBuildingDrawer().getPosition().getX(), towerGraphicHelper.getBuildingDrawer().getPosition().getY());
        drawer.setLayer(layer);
    }

    Soldier targetSoldier;

    public void startNewWave(final PointF start, final PointF end, Soldier soldier)
    {
        this.start = start;
        this.end = end;
        targetSoldier = soldier;
        if (soldier != null)
            setUpCosSin(start, end);
        hitTarget = false;
        inProgress = true;
    }

    private void setUpCosSin(PointF start, PointF end)
    {
        double distance = PointF.euclideanDistance(start, end);
        PointF location = targetSoldier.getAttackHelper().getGraphicHelper().getDrawer().getPosition();
        cos = (location.getX() - start.getX()) / distance;
        sin = (location.getY() - start.getY()) / distance;
    }

    @Override
    public void update(double deltaT)
    {
        doReplacing(deltaT);
    }

    public void doReplacing(double deltaT)
    {
        System.out.println(toString());
        if (end == null || start == null)
            return;
        if (hitTarget)
            return;
        if (PointF.euclideanDistance(drawer.getPosition(), end) < .3)
        {
            hitTarget = true;
            drawer.setPosition(towerGraphicHelper.getBuildingDrawer().getPosition().getX(), towerGraphicHelper.getBuildingDrawer().getPosition().getY());
            inProgress = false;
            towerGraphicHelper.onBulletHit(DefenseKind.SINGLE_TARGET);
            start = null;
            end = null;
            targetSoldier = null;
            cos = -1;
            sin = -1;
        }
        double step = deltaT * speed;
        drawer.getDrawable().setRotation(getAngle(sin, cos));
        setUpCosSin(drawer.getPosition(), new PointF(targetSoldier.getLocation()));
        drawer.setPosition(drawer.getPosition().getX() + step * cos, drawer.getPosition().getY() + step * sin);

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