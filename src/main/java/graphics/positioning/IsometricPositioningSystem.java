package graphics.positioning;

import utils.PointF;

public class IsometricPositioningSystem extends PositioningSystem
{
    private static final double angle = 37;

    public static final double ANG_SIN = Math.sin(angle * Math.PI / 180);
    public static final double ANG_COS = Math.cos(angle * Math.PI / 180);

    private static IsometricPositioningSystem instance;

    private double scale;

    public IsometricPositioningSystem(double scale)
    {
        this.scale = scale;
    }

    public static IsometricPositioningSystem getInstance()
    {
        if (instance == null)
            instance = new IsometricPositioningSystem(sScale);
        return instance;
    }

    @Override
    public double convertX(PointF position)
    {
        return scale * (position.getX() + position.getY() + 1 /* center of square */) * ANG_COS;
    }

    @Override
    public double convertY(PointF position)
    {
        return scale * (-position.getX() + position.getY()) * ANG_SIN;
    }
}
