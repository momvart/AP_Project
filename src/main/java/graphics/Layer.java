package graphics;

import graphics.drawers.Drawer;
import graphics.drawers.drawables.IDrawable;
import javafx.scene.canvas.GraphicsContext;
import utils.RectF;

import java.util.ArrayList;

public class Layer implements IFrameUpdatable
{
    private int order;
    private RectF bounds;

    private ArrayList<Drawer> drawers = new ArrayList<>();
    private ArrayList<IFrameUpdatable> updatables = new ArrayList<>();

    public Layer(int order, RectF bounds)
    {
        this.order = order;
        this.bounds = bounds;
    }

    public int getOrder()
    {
        return order;
    }

    public void addObject(Drawer drawer)
    {
        drawers.add(drawer);
        if (drawer instanceof IFrameUpdatable)
            updatables.add((IFrameUpdatable)drawer);
    }


    public void draw(GraphicsContext gc, RectF cameraBounds)
    {
        gc.save();
        gc.translate(bounds.getX(), bounds.getY());
        RectF relBounds = new RectF(cameraBounds.getX() - bounds.getX(), cameraBounds.getY() - bounds.getY(), cameraBounds.getWidth(), cameraBounds.getHeight());
        drawers.stream()
                .filter(d -> d.isVisible() && d.canBeVisibleIn(relBounds))
                .forEach(d -> d.draw(gc));
        gc.restore();
    }

    @Override
    public void update(double deltaT)
    {
        updatables.forEach(d -> d.update(deltaT));
    }
}
