package graphics.drawers.drawables;

import utils.PointF;
import utils.SizeF;

public abstract class Drawable implements IDrawable
{
    private PointF pivot = new PointF(0.5f, 0.5f);
    private SizeF size = new SizeF(0, 0);

    public PointF getPivot()
    {
        return pivot;
    }

    public void setPivot(PointF pivot)
    {
        this.pivot = pivot;
    }

    public SizeF getSize()
    {
        return size;
    }

    public void setSize(float width, float height)
    {
        this.size.setWidth(width);
        this.size.setHeight(height);
    }
}
