package graphics.helpers;

import graphics.IFrameUpdatable;
import graphics.drawers.Drawer;
import graphics.drawers.drawables.ImageDrawable;
import graphics.layers.Layer;
import utils.GraphicsUtilities;
import utils.PointF;

import java.net.URISyntaxException;

public class BulletHelper implements IFrameUpdatable
{
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


    public void startNewWave(final PointF start, final PointF end)
    {
        System.out.println("on new wave we are ");
        this.start = start;
        this.end = end;
        setUpCosSin(start, end);
        hitTarget = false;
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
        if (PointF.euclideanDistance(drawer.getPosition(), end) < .5)
        {
            System.out.println("move has terminated ");
            hitTarget = true;
            drawer.setPosition(towerGraphicHelper.getBuildingDrawer().getPosition().getX(), towerGraphicHelper.getBuildingDrawer().getPosition().getY());
            towerGraphicHelper.onBulletHit(DefenseKind.SINGLE_TARGET);
        }
        if (hitTarget)
            return;
        double step = deltaT * speed;
        drawer.setPosition(drawer.getPosition().getX() + step * cos, drawer.getPosition().getY() + step * sin);
    }
}