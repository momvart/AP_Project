package graphics.positioning;

import utils.PointF;

public abstract class PositioningSystem
{
    public static double sScale = 1;

    public abstract double convertX(PointF position);

    public abstract double convertY(PointF position);
}
