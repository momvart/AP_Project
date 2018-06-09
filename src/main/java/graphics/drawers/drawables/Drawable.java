package graphics.drawers.drawables;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.transform.*;
import utils.*;

public abstract class Drawable implements IDrawable
{
    private PointF pivot = new PointF(0.5f, 0.5f);
    private SizeF size = new SizeF(0, 0);

    private Translate translate = new Translate();
    private Scale scale = new Scale();
    private Rotate rotate = new Rotate();


    public PointF getPivot()
    {
        return pivot;
    }

    public void setPivot(PointF pivot)
    {
        this.pivot = pivot;
        setPivots();
    }

    public SizeF getSize()
    {
        return size;
    }

    public void setSize(float width, float height)
    {
        this.size.setWidth(width);
        this.size.setHeight(height);
        setPivots();
    }

    private void setPivots()
    {
        translate.setX(-getPivot().getX() * getSize().getWidth());
        translate.setY(-getPivot().getY() * getSize().getHeight());

        rotate.setPivotX(pivot.getX() * size.getWidth());
        rotate.setPivotY(pivot.getY() * size.getHeight());

        scale.setPivotX(rotate.getPivotX());
        scale.setPivotY(rotate.getPivotY());
    }

    public void setRotation(double angle)
    {
        rotate.setAngle(angle);
    }

    public void setScale(double x, double y)
    {
        scale.setX(x);
        scale.setY(y);
    }

    protected void onPreDraw(GraphicsContext gc)
    {
        gc.save();
        GraphicsUtilities.GCTransform(gc, translate);
        GraphicsUtilities.GCTransform(gc, rotate);
        GraphicsUtilities.GCTransform(gc, scale);
    }

    protected abstract void onDraw(GraphicsContext gc);

    protected void onPostDraw(GraphicsContext gc)
    {
        gc.restore();
    }

    @Override
    public void draw(GraphicsContext gc)
    {
        onPreDraw(gc);
        onDraw(gc);
        onPostDraw(gc);
    }
}
