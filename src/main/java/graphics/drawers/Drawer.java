package graphics.drawers;

import graphics.drawers.drawables.Drawable;
import javafx.scene.canvas.GraphicsContext;
import utils.PointF;
import utils.RectF;

public class Drawer
{
    private Drawable drawable;

    private boolean visible = true;
    private PointF position = new PointF(0, 0);


    public Drawer(Drawable drawable)
    {
        this.drawable = drawable;
    }


    public PointF getPosition()
    {
        return position;
    }

    public void setPosition(float x, float y)
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
        return scene.intersectsWith(position.getX(), position.getY(), drawable.getSize().getWidth(), drawable.getSize().getHeight());
    }

    public void draw(GraphicsContext gc)
    {
        if (!visible)
            return;

        gc.save();
        gc.translate(position.getX(), position.getY());
        drawable.draw(gc);
        gc.restore();
    }
}
