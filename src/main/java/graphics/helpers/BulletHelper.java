package graphics.helpers;

import graphics.IFrameUpdatable;
import graphics.drawers.Drawer;
import graphics.layers.Layer;
import utils.GraphicsUtilities;
import utils.PointF;

import java.net.URISyntaxException;

public class BulletHelper implements IFrameUpdatable
{
    protected DefensiveTowerGraphicHelper towerGraphicHelper;
    protected final double maxSpeed = 1;//TODO‌ to be manipulated
    protected Drawer drawer;
    protected PointF start;
    protected PointF end;
    protected double speed = 1;
    protected boolean hitTarget = false;
    protected double cos;
    protected double sin;

    public BulletHelper(DefensiveTowerGraphicHelper towerGraphicHelper, Layer layer)
    {
        this.towerGraphicHelper = towerGraphicHelper;

        try
        {
            drawer = new Drawer(GraphicsUtilities.createImageDrawable("assets/bullets/arrow.png", 10, 10, true, 0.5, 0.5));
        }
        catch (URISyntaxException e) {}

        setUpBulletProperties(layer);

    }

    public void setUpBulletProperties(Layer layer)
    {
        drawer.setPosition(towerGraphicHelper.getBuildingDrawer().getPosition().getX(), towerGraphicHelper.getBuildingDrawer().getPosition().getY());
        drawer.setLayer(layer);
    }

    public void startNewWave(PointF start, PointF end)
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
        System.out.println("update on single target bullet....");
        doReplacing(deltaT);
    }

    public void doReplacing(double deltaT)
    {
        System.out.println("on replacing we are ");
        if (end == null || start == null)
            return;
        if (PointF.euclideanDistance(drawer.getPosition(), end) < .1)
        {
            System.out.println("move has terminated ");
            hitTarget = true;
            drawer.setPosition(towerGraphicHelper.getBuildingDrawer().getPosition().getX(), towerGraphicHelper.getBuildingDrawer().getPosition().getY());
            towerGraphicHelper.onBulletHit(DefenseKind.SINGLE_TARGET);
        }
        if (hitTarget)
            return;
        double step = deltaT * speed;
        System.out.println("step is :" + step);
        drawer.setPosition(drawer.getPosition().getX() + step * cos, drawer.getPosition().getY() + step * sin);
        System.out.println("drawer new position is :" + drawer.getPosition());
    }
}