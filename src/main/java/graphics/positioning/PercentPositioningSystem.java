package graphics.positioning;

import graphics.Layer;
import utils.PointF;


/**
 * A positioning system that depends on size of the layer.
 * Positions should be between 0 and 1
 */
public class PercentPositioningSystem extends PositioningSystem
{
    private Layer layer;

    public PercentPositioningSystem(Layer layer)
    {
        this.layer = layer;
    }

    @Override
    public double convertX(PointF position)
    {
        return position.getX() * layer.getBounds().getWidth();
    }

    @Override
    public double convertY(PointF position)
    {
        return position.getY() * layer.getBounds().getHeight();
    }
}
