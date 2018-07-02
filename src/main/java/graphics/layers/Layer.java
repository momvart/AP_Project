package graphics.layers;

import graphics.GameScene;
import graphics.IFrameUpdatable;
import graphics.drawers.Drawer;
import graphics.drawers.drawables.IAlphaDrawable;
import graphics.positioning.NormalPositioningSystem;
import graphics.positioning.PositioningSystem;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import utils.RectF;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;

public class Layer implements IFrameUpdatable, IAlphaDrawable
{
    private GameScene scene;

    private int order;
    private RectF bounds;

    private boolean visible = true;
    private double alpha = 1;

    private ArrayList<Drawer> drawers = new ArrayList<>();

    private PositioningSystem posSys;

    public Layer(int order, RectF bounds)
    {
        this(order, bounds, new NormalPositioningSystem(1));
    }

    public Layer(int order, RectF bounds, PositioningSystem posSys)
    {
        this.order = order;
        this.bounds = bounds;
        this.posSys = posSys;
    }

    public GameScene getScene()
    {
        return scene;
    }

    public void setScene(GameScene scene)
    {
        this.scene = scene;
    }

    public int getOrder()
    {
        return order;
    }

    public RectF getBounds()
    {
        return bounds;
    }

    public double getWidth()
    {
        return getBounds().getWidth();
    }

    public double getHeight()
    {
        return getBounds().getHeight();
    }

    public boolean isVisible()
    {
        return visible;
    }

    public void setVisible(boolean visible)
    {
        this.visible = visible;
    }

    @Override
    public void setAlpha(double alpha)
    {
        this.alpha = alpha;
    }

    public void setBounds(RectF bounds)
    {
        this.bounds = bounds;
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
        if (drawer.isClickable())
            addClickable(drawer);
    }

    public void removeObject(Drawer drawer)
    {
        drawers.remove(drawer);
    }

    public void removeAllObjects()
    {
        drawers.clear();
        clickables.clear();
    }

    private HashSet<Drawer> clickables = new HashSet<>();

    public void addClickable(Drawer clickable)
    {
        clickables.add(clickable);
    }

    public boolean handleMouseClick(double x, double y, MouseEvent event)
    {
        for (Drawer clickable : clickables)
            if (clickable.containsPoint(x - bounds.getX(), y - bounds.getY()))
            {
                clickable.callOnClick(event);
                return true;
            }
        return false;
    }

    @Override
    public void draw(GraphicsContext gc)
    {
        throw new UnsupportedOperationException();
    }

    public void draw(GraphicsContext gc, RectF cameraBounds)
    {
        if (!isVisible())
            return;
        gc.save();
        gc.setGlobalAlpha(gc.getGlobalAlpha() * alpha);
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
        drawers.forEach(d -> d.update(deltaT));
    }
}
