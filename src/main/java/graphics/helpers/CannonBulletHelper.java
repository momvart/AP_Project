package graphics.helpers;

import graphics.drawers.Drawer;
import graphics.layers.Layer;
import models.soldiers.Soldier;
import utils.GraphicsUtilities;
import utils.PointF;

import java.net.URISyntaxException;

import static java.lang.Math.abs;
import static java.lang.Math.sqrt;

public class CannonBulletHelper extends BulletHelper
{
    private final double height = 5;
    private boolean reachedVertex;
    private PointF vertex;
    private double verticalSpeed = 2;
    private double heightDifferanceToEnd;
    private double heightDifferanceToStart;

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
        return new PointF(pointF.getX() + height, pointF.getY() - height);
    }

    @Override
    public void startNewWave(final PointF start, final PointF end, final Soldier soldier)
    {
        System.out.println("new wave requested ...................");
        super.startNewWave(start, end, soldier);
        reachedVertex = false;
        hitTarget = false;
        vertex = getVertexOfParabola(start, end);
        heightDifferanceToEnd = abs(vertex.getY() - end.getY());
        heightDifferanceToStart = abs(vertex.getY() - start.getY());
        double distance = PointF.euclideanDistance(start, vertex);
        cos = (vertex.getX() - start.getX()) / distance;
        sin = (vertex.getY() - start.getY()) / distance;
        inProgress = true;
    }

    @Override
    public void doReplacing(double deltaT)
    {
        if (drawer.getPosition() == null || vertex == null)
            return;
        if (hitTarget)
            return;
        if (!reachedVertex && PointF.euclideanDistance(drawer.getPosition(), vertex) < 1)
        {
            System.out.println(toString() + "on fuckin  here ");
            reachedVertex = true;
            double distance = PointF.euclideanDistance(drawer.getPosition(), end);
            cos = (end.getX() - drawer.getPosition().getX()) / distance;
            sin = (end.getY() - drawer.getPosition().getY()) / distance;
        }
        verticalSpeed = reachedVertex ?
                sqrt(abs(vertex.getY() - drawer.getPosition().getY()) / heightDifferanceToEnd) * maxSpeed
                : -sqrt(abs(vertex.getY() - drawer.getPosition().getY()) / heightDifferanceToStart) * maxSpeed;

        speed = abs((1 / sin) * verticalSpeed);

        double verticalStep = deltaT * verticalSpeed;
        double horizontalStep = deltaT * speed * cos;
        drawer.setPosition(drawer.getPosition().getX() + horizontalStep, drawer.getPosition().getY() + verticalStep);
        System.out.println("drawer position is :" + drawer.getPosition() + " and vertex is : " + vertex + " and end is :" + end);
        System.out.println("cos and sin are " + sin + "        " + cos);
        if (reachedVertex && PointF.euclideanDistance(drawer.getPosition(), end) < .5)
        {
            vertex = null;
            hitTarget = true;
            drawer.setPosition(towerGraphicHelper.getBuildingDrawer().getPosition().getX(), towerGraphicHelper.getBuildingDrawer().getPosition().getY());
            inProgress = false;
            System.out.println("in progress offed ");
            towerGraphicHelper.onBulletHit(DefenseKind.AREA_SPLASH);
            start = null;
            end = null;
            targetSoldier = null;
            cos = -1;
            sin = -1;
        }
    }

    @Override
    public String toString()
    {
        return "CannonBulletHelper{" +
                "height=" + height +
                ", reachedVertex=" + reachedVertex +
                ", vertex=" + vertex +
                ", verticalSpeed=" + verticalSpeed +
                '}' + super.toString();
    }
}