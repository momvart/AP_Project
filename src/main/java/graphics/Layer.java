package graphics;

import graphics.drawers.Drawer;
import graphics.positioning.*;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import utils.*;

import java.util.*;

public class Layer implements IFrameUpdatable
{
    private int order;
    private RectF bounds;

    private ArrayList<Drawer> drawers = new ArrayList<>();
    private ArrayList<IFrameUpdatable> updatables = new ArrayList<>();

    private PositioningSystem posSys;

    public Layer(int order, RectF bounds)
    {
        this(order, bounds, NormalPositioningSystem.getInstance());
    }

    public Layer(int order, RectF bounds, PositioningSystem posSys)
    {
        this.order = order;
        this.bounds = bounds;
        this.posSys = posSys;
    }

    public int getOrder()
    {
        return order;
    }

    public RectF getBounds()
    {
        return bounds;
    }

    public PositioningSystem getPosSys()
    {
        return posSys;
    }

    public void setPosSys(PositioningSystem posSys)
    {
        this.posSys = posSys;
    }

    public void addObject(Drawer drawer)
    {
        drawers.add(drawer);
        if (drawer instanceof IFrameUpdatable)
            updatables.add((IFrameUpdatable)drawer);
        if (drawer.isClickable())
            addClickable(drawer);
    }

    public void removeObject(Drawer drawer)
    {
        drawers.remove(drawer);
        if (drawer instanceof IFrameUpdatable)
            updatables.remove(drawer);
    }

    private HashSet<Drawer> clickables = new HashSet<>();

    public void addClickable(Drawer clickable)
    {
        clickables.add(clickable);
    }

    public boolean handleMouseClick(double x, double y, MouseEvent event)
    {
        RectF mousePoint = new RectF(x - bounds.getX(), y - bounds.getY(), 0, 0);
        for (Drawer clickable : clickables)
            if (clickable.canBeVisibleIn(mousePoint))
            {
                clickable.callOnClick(event);
                return true;
            }
        return false;
    }

    public void draw(GraphicsContext gc, RectF cameraBounds)
    {
        gc.save();
        gc.translate(bounds.getX(), bounds.getY());
        RectF relBounds = new RectF(cameraBounds.getX() - bounds.getX(), cameraBounds.getY() - bounds.getY(), cameraBounds.getWidth(), cameraBounds.getHeight());
        drawers.stream()
                .filter(d -> d.isVisible()/* && d.canBeVisibleIn(bounds)*/ && d.canBeVisibleIn(relBounds))
                .sorted(Comparator.comparing(d -> posSys.convertY(d.getPosition()))) //TODO: not optimized
                .forEach(d -> d.draw(gc));
        gc.restore();
    }

    @Override
    public void update(double deltaT)
    {
        updatables.forEach(d -> d.update(deltaT));
    }
}
