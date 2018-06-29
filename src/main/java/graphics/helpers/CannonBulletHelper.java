package graphics.helpers;

import graphics.drawers.Drawer;
import graphics.layers.Layer;
import utils.GraphicsUtilities;
import utils.PointF;

import java.net.URISyntaxException;

import static java.lang.Math.abs;
import static java.lang.Math.sqrt;

public class CannonBulletHelper extends BulletHelper
{
    private final double height = 7;
    boolean reachedVertex;
    private PointF vertex;
    private double verticalSpeed;

    public CannonBulletHelper(DefensiveTowerGraphicHelper towerGraphicHelper, Layer layer)
    {
        super(towerGraphicHelper, layer);

        try
        {
            drawer = new Drawer(GraphicsUtilities.createImageDrawable("assets/bullets/cannonBulletWhite.png", 10, 10, true, .5, .5));
            drawer.addUpdatable(this);
        }
        catch (URISyntaxException e)
        {
        }
        setUpBulletProperties(layer);
    }

    @Override
    public void setUpBulletProperties(Layer layer)
    {
        drawer.setPosition(towerGraphicHelper.getBuildingDrawer().getPosition().getX(), towerGraphicHelper.getBuildingDrawer().getPosition().getY());
        drawer.setLayer(layer);
    }

    private PointF getVertexOfParabola(PointF start, PointF end)
    {
        PointF middle = new PointF((start.getX() + end.getX()) / 2, (start.getY() + end.getY()) / 2);
        PointF pointF;
        PointF upper = start.getY() >= end.getY() ? start : end;
        pointF = new PointF((upper.getX() + middle.getX() - upper.getY() + middle.getY()) / 2, (middle.getX() + middle.getY() - upper.getX() + upper.getY()) / 2);
        PointF vertex = new PointF(pointF.getX() + height, pointF.getY() - height);
        System.out.println("vertex is: " + vertex);
        return vertex;
    }

    @Override
    public void startNewWave(PointF start, PointF end)
    {
        super.startNewWave(start, end);
        reachedVertex = false;
        vertex = getVertexOfParabola(start, end);
        double distance = PointF.euclideanDistance(start, vertex);
        cos = (vertex.getX() - start.getX()) / distance;
        sin = (vertex.getY() - start.getY()) / distance;
    }

    @Override
    public void doReplacing(double deltaT)
    {
        if (hitTarget)
            return;
        if (reachedVertex && PointF.euclideanDistance(drawer.getPosition(), end) < .5)
        {
            hitTarget = true;
            drawer.setPosition(towerGraphicHelper.getBuildingDrawer().getPosition().getX(), towerGraphicHelper.getBuildingDrawer().getPosition().getY());
            towerGraphicHelper.onBulletHit(DefenseKind.AREA_SPLASH);
        }
        if (drawer.getPosition() == null || vertex == null)
            return;
        if (PointF.euclideanDistance(drawer.getPosition(), vertex) < .1)
        {
            reachedVertex = true;
            double distance = PointF.euclideanDistance(drawer.getPosition(), end);
            cos = (end.getX() - drawer.getPosition().getX()) / distance;
            sin = (end.getY() - drawer.getPosition().getY()) / distance;
        }
        verticalSpeed = reachedVertex ?
                -sqrt((vertex.getY() - drawer.getPosition().getY()) / abs(vertex.getY() - end.getY())) * maxSpeed
                : sqrt(vertex.getY() - drawer.getPosition().getY() / abs(vertex.getY() - start.getY())) * maxSpeed;
        speed = abs((1 / sin) * verticalSpeed);
        double verticalStep = deltaT * verticalSpeed;
        double horizontalStep = deltaT * speed * cos;
        drawer.setPosition(drawer.getPosition().getX() + verticalStep, drawer.getPosition().getY() + horizontalStep);
    }

}