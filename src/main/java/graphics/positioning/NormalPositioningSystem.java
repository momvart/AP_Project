package graphics.positioning;

import utils.PointF;

public class NormalPositioningSystem extends PositioningSystem
{
    private static NormalPositioningSystem instance;

    private double scale;

    public NormalPositioningSystem(double scale)
    {
        this.scale = scale;
    }

    public static NormalPositioningSystem getInstance()
    {
        if (instance == null)
            instance = new NormalPositioningSystem(sScale);
        return instance;
    }

    public double getScale()
    {
        return scale;
    }

    @Override
    public double convertX(PointF position)
    {
        return scale * position.getX();
    }

    @Override
    public double convertY(PointF position)
    {
        return scale * position.getY();
    }
}
