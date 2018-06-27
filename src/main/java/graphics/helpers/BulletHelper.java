package graphics.helpers;

import graphics.IFrameUpdatable;
import graphics.drawers.Drawer;
import utils.PointF;

public class BulletHelper implements IFrameUpdatable
{
    protected final double maxSpeed = 1;//TODO‌ to be manipulated
    protected Drawer drawer;
    protected PointF start;
    protected PointF end;
    protected double speed = 1;
    protected boolean hitTarget = false;
    protected double cos;
    protected double sin;

    public BulletHelper()
    {
    }

    public void startNewWave(PointF start, PointF end)
    {
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
        if (hitTarget)
            return;
        double step = deltaT * speed;
        drawer.setPosition(drawer.getPosition().getX() + step * cos, drawer.getPosition().getY() + step * sin);
    }
}
