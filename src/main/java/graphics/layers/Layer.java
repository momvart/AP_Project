package graphics.layers;

import graphics.GameScene;
import graphics.IFrameUpdatable;
import graphics.drawers.Drawer;
import graphics.drawers.drawables.IAlphaDrawable;
import graphics.positioning.NormalPositioningSystem;
import graphics.positioning.PositioningSystem;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import utils.RectF;

import java.util.*;

public class Layer implements IFrameUpdatable, IAlphaDrawable
{
    private GameScene scene;

    private int order;
    private RectF bounds;
    private boolean hardBound;

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

    public void setBounds(RectF bounds)
    {
        this.bounds = bounds;
    }

    public void setHardBound(boolean hardBound)
    {
        this.hardBound = hardBound;
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

    //region Scroll

    private boolean hScrollable = false, vScrollable = false;
    private boolean dynamicScroll = false;
    private double scrollX, scrollY;
    private double maxXScroll = Double.NEGATIVE_INFINITY, minXScroll = Double.POSITIVE_INFINITY, maxYScroll = Double.NEGATIVE_INFINITY, minYScroll = Double.POSITIVE_INFINITY;

    public boolean isScrollable()
    {
        return hScrollable || vScrollable;
    }

    public void setHorizontallyScrollable(boolean scrollable)
    {
        this.hScrollable = scrollable;
    }

    public void setVerticallyScrollable(boolean scrollable) { this.vScrollable = scrollable; }

    public void setScroll(double x, double y)
    {
        if (dynamicScroll)
            updateScrollBounds();
        if (hScrollable)
            this.scrollX = Math.max(-maxXScroll, Math.min(-minXScroll, x));
        if (vScrollable)
            this.scrollY = Math.max(-maxYScroll, Math.min(-minYScroll, y)); ;
    }

    private void updateScrollBounds()
    {
        for (Drawer drawer : drawers)
        {
            double x = getPosSys().convertX(drawer.getPosition());
            double y = getPosSys().convertY(drawer.getPosition());

            minXScroll = Math.min(minXScroll, x);
            minYScroll = Math.min(minYScroll, y);
            if (drawer.getDrawable() == null)
                continue;
            maxXScroll = Math.max(maxXScroll, x + drawer.getDrawable().getWidth() - bounds.getWidth());
            maxYScroll = Math.max(maxYScroll, y + drawer.getDrawable().getHeight() - bounds.getHeight());
        }
    }

    public void setDynamicScroll(boolean dynamicScroll)
    {
        this.dynamicScroll = dynamicScroll;
    }

    public boolean handleScroll(double x, double y, ScrollEvent event)
    {
        if (!isScrollable() || !bounds.contains(x, y))
            return false;

        if (hScrollable)
            setScroll(scrollX + event.getDeltaX(), scrollY);
        if (vScrollable)
            setScroll(scrollX, scrollY + event.getDeltaY());
        return true;
    }

    //endregion

    public PositioningSystem getPosSys()
    {
        return posSys;
    }

    public void setPosSys(PositioningSystem posSys)
    {
        this.posSys = posSys;
    }


    private Queue<Drawer> pendingAdds = new ArrayDeque<>();

    public void addObject(Drawer drawer)
    {
        drawers.add(drawer);
        if (drawer.isClickable())
            addClickable(drawer);
        if (!dynamicScroll)
            updateScrollBounds();
    }

    public void addObject(Drawer drawer, boolean putInQue)
    {
        if (!putInQue)
        {
            addObject(drawer);
            return;
        }
        pendingAdds.add(drawer);
        if (drawer.isClickable())
            addClickable(drawer);
        if (!dynamicScroll)
            updateScrollBounds();
    }

    private Queue<Drawer> pendingRemoves = new ArrayDeque<>();

    public void removeObject(Drawer drawer)
    {
        pendingRemoves.add(drawer);
        if (drawer.isClickable())
            clickables.remove(drawer);
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
            if (clickable.containsPoint(x - bounds.getX() - scrollX, y - bounds.getY() - scrollY))
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
        gc.translate(scrollX, scrollY);
        RectF relBounds = new RectF(cameraBounds.getX() - bounds.getX() - scrollX, cameraBounds.getY() - bounds.getY() - scrollY, cameraBounds.getWidth(), cameraBounds.getHeight());
        RectF layerBounds = new RectF(-scrollX, -scrollY, getWidth(), getHeight());
        drawers.stream()
                .filter(d -> d.isVisible() && d.canBeVisibleIn(relBounds) && (!hardBound || d.canBeVisibleIn(layerBounds)))
                .sorted(Comparator.comparing(d -> posSys.convertY(d.getPosition()))) //TODO: not optimized
                .forEach(d -> d.draw(gc));
        gc.restore();
    }

    @Override
    public void update(double deltaT)
    {
        for (int i = 0; i < pendingAdds.size(); i++)
            drawers.add(pendingAdds.poll());
        for (int i = 0; i < pendingRemoves.size(); i++)
            drawers.remove(pendingRemoves.poll());
        drawers.forEach(d -> d.update(deltaT));
    }
}
