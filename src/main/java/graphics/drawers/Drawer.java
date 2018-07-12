package graphics.drawers;

import graphics.IFrameUpdatable;
import graphics.layers.Layer;
import graphics.drawers.drawables.Drawable;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import utils.PointF;
import utils.RectF;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class Drawer implements IFrameUpdatable
{
    private Drawable drawable;

    private boolean visible = true;
    private PointF position = new PointF(0, 0);

    private Layer layer;

    private ArrayList<IFrameUpdatable> updatables = new ArrayList<>();

    public Drawer() {}

    public Drawer(Drawable drawable)
    {
        this.drawable = drawable;
    }

    public Drawable getDrawable()
    {
        return drawable;
    }

    public void setDrawable(Drawable drawable)
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

    public void setVisible(boolean visible)
    {
        this.visible = visible;
    }

    public RectF getBounds()
    {
        return new RectF(layer.getPosSys().convertX(position), layer.getPosSys().convertY(position), getDrawable().getWidth(), getDrawable().getHeight());
    }

    public boolean canBeVisibleIn(RectF scene) //TODO: has bug
    {
        if (getDrawable() == null)
            return false;
        return scene.intersectsWith(layer.getPosSys().convertX(position) + getDrawable().getTranslate().getX(),
                layer.getPosSys().convertY(position) + getDrawable().getTranslate().getY(),
                getDrawable().getWidth(),
                getDrawable().getHeight());
    }

    public boolean containsPoint(double x, double y)
    {
        if (getDrawable() == null)
            return false;
        try
        {
            Point2D newPoint = getDrawable().getTranslate().inverseTransform(x - layer.getPosSys().convertX(position), y - layer.getPosSys().convertY(position));
            newPoint = getDrawable().getRotate().inverseTransform(newPoint);
            newPoint = getDrawable().getScale().inverseTransform(newPoint);
            return newPoint.getX() >= 0 && newPoint.getY() >= 0 && newPoint.getX() <= getDrawable().getWidth() && newPoint.getY() <= getDrawable().getHeight();
        }
        catch (Exception ignored) {}
        return false;
    }

    public Layer getLayer()
    {
        return layer;
    }

    public void setLayer(Layer layer)
    {
        if (this.layer != null)
            this.layer.removeObject(this);
        this.layer = layer;
        layer.addObject(this);
    }

    private boolean clickable = false;

    public boolean isClickable()
    {
        return clickable;
    }

    private IDrawerClickListener clickListener;

    public void setClickListener(IDrawerClickListener clickListener)
    {
        this.clickListener = clickListener;
        clickable = clickListener != null;
    }

    public void callOnClick(MouseEvent event)
    {
        if (clickListener != null)
            clickListener.click(this, event);
    }

    protected void onPreDraw(GraphicsContext gc)
    {
        gc.save();
        if (layer == null)
            gc.translate(position.getX(), position.getY());
        else
            gc.translate(layer.getPosSys().convertX(position), layer.getPosSys().convertY(position));
    }

    protected void onDraw(GraphicsContext gc)
    {
        if (!visible || getDrawable() == null)
            return;
        getDrawable().draw(gc);
    }

    private Queue<IFrameUpdatable> pendingAdds = new LinkedList<>();

    public void addUpdatable(IFrameUpdatable updatable)
    {
        pendingAdds.add(updatable);
    }

    private Queue<IFrameUpdatable> pendingRemoves = new LinkedList<>();

    public void removeUpdatable(IFrameUpdatable updatable) { pendingRemoves.add(updatable); }

    @Override
    public void update(double deltaT)
    {
        for (int i = 0; i < pendingAdds.size(); i++)
            updatables.add(pendingAdds.poll());
        for (int i = 0; i < pendingRemoves.size(); i++)
            updatables.remove(pendingRemoves.poll());

        updatables.forEach(u -> u.update(deltaT));
    }

    protected void onPostDraw(GraphicsContext gc)
    {
        gc.restore();
    }

    public void draw(GraphicsContext gc)
    {
        onPreDraw(gc);
        onDraw(gc);
        onPostDraw(gc);
    }
}
