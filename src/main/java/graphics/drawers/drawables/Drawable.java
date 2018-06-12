package graphics.drawers.drawables;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.transform.*;
import utils.*;

public abstract class Drawable implements IDrawable
{
    private PointF pivot = new PointF(0f, 0f);
    private SizeF size = new SizeF(0, 0);

    private Translate translate = new Translate();
    private Scale scale = new Scale();
    private Rotate rotate = new Rotate();


    public PointF getPivot()
    {
        return pivot;
    }

    public void setPivot(double x, double y)
    {
        this.pivot.setX(x);
        this.pivot.setY(y);
        setPivots();
    }

    public SizeF getSize()
    {
        return size;
    }

    public void setSize(double width, double height)
    {
        this.size.setWidth(width);
        this.size.setHeight(height);
        setPivots();
    }

    public double getWidth()
    {
        return size.getWidth();
    }

    public double getHeight() { return size.getHeight(); }

    private void setPivots()
    {
        translate.setX(-getPivot().getX() * getWidth());
        translate.setY(-getPivot().getY() * getHeight());

        rotate.setPivotX(pivot.getX() * getWidth());
        rotate.setPivotY(pivot.getY() * getHeight());

        scale.setPivotX(rotate.getPivotX());
        scale.setPivotY(rotate.getPivotY());
    }

    public Translate getTranslate()
    {
        return translate;
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

        gc.setFill(Color.BLACK);
        gc.setStroke(Color.BLACK);
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
