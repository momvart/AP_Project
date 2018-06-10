package graphics.positioning;

import utils.PointF;

public class IsometricPositioningSystem extends PositioningSystem
{
    private static final double Sin30 = 0.5f;
    private static final double Cos30 = 0.866f;

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
        return scale * (position.getX() + position.getY()) * Cos30 + 100;
    }

    @Override
    public double convertY(PointF position)
    {
        return scale * (-position.getX() + position.getY()) * Sin30 + 100;
    }
}
