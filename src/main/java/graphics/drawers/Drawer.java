package graphics.drawers;

import graphics.Layer;
import graphics.drawers.drawables.Drawable;
import javafx.scene.canvas.GraphicsContext;
import utils.PointF;
import utils.RectF;

public class Drawer
{
    private Drawable drawable;

    private boolean visible = true;
    private PointF position = new PointF(0, 0);

    private Layer layer;

    public Drawer(Drawable drawable)
    {
        this.drawable = drawable;
    }

    public PointF getPosition()
    {
        return position;
    }

    public void setPosition(double x, double y)
    {
        position.setX(x);
        position.setY(y);
    }

    public boolean isVisible()
    {
        return visible;
    }

    public boolean canBeVisibleIn(RectF scene)
    {
        return scene.intersectsWith(position.getX(), position.getY(), drawable.getWidth(), drawable.getHeight());
    }

    public Layer getLayer()
    {
        return layer;
    }

    public void setLayer(Layer layer)
    {
        if (layer != null)
            layer.removeObject(this);
        this.layer = layer;
        layer.addObject(this);
    }

    public void draw(GraphicsContext gc)
    {
        if (!visible)
            return;

        gc.save();
        if (layer == null)
            gc.translate(position.getX(), position.getY());
        else
            gc.translate(layer.getPosSys().convertX(position), layer.getPosSys().convertY(position));
        drawable.draw(gc);
        gc.restore();
    }
}
