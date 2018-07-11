package graphics;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import utils.RectF;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class GraphicHandler implements IFrameUpdatable
{
    private GraphicsContext gc;
    private GameScene scene;

    private boolean disableDraws = false;

    private ArrayList<IFrameUpdatable> updatables = new ArrayList<>();

    public GraphicHandler(GraphicsContext gc, RectF camera)
    {
        this.gc = gc;
        gc.save();
        setScale(GraphicsValues.getScale());
        updateCamera(camera);
    }

    public void setScene(GameScene scene)
    {
        this.scene = scene;
    }

    private double scale = 1;

    public void setScale(double scale)
    {
        gc.restore();
        gc.save();
        gc.transform(scale, 0, 0, scale, 0, 0);
        this.scale = scale;
    }

    public void setDisableDraws(boolean disableDraws)
    {
        this.disableDraws = disableDraws;
    }

    private RectF cameraBounds = new RectF(0, 0, 0, 0);

    public void updateCamera(RectF newBounds)
    {
        double ratio = 1 / scale;
        newBounds.setX(newBounds.getX() * ratio);
        newBounds.setY(newBounds.getY() * ratio);
        gc.translate(cameraBounds.getX() - newBounds.getX(), cameraBounds.getY() - newBounds.getY());
        cameraBounds = newBounds;
        cameraBounds.setWidth(cameraBounds.getWidth() * ratio);
        cameraBounds.setHeight(cameraBounds.getHeight() * ratio);
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
        scene.update(deltaT);
        if (!disableDraws)
            scene.draw(gc, cameraBounds);
    }

    public void handleMouseClick(MouseEvent event)
    {
        try
        {
            Point2D point = gc.getTransform().inverseTransform(event.getX(), event.getY());
            scene.handleMouseClick(point.getX(), point.getY(), event);
        }
        catch (Exception ex) { ex.printStackTrace(); }
    }

    public boolean handleMouseClickResult(MouseEvent event)
    {
        try
        {
            Point2D point = gc.getTransform().inverseTransform(event.getX(), event.getY());
            return scene.handleMouseClick(point.getX(), point.getY(), event);
        }
        catch (Exception ex) { ex.printStackTrace(); return false;}
    }

    public boolean handleScrollResult(ScrollEvent event)
    {
        try
        {
            Point2D point = gc.getTransform().inverseTransform(event.getX(), event.getY());
            return scene.handleScroll(point.getX(), point.getY(), event);
        }
        catch (Exception ex) { ex.printStackTrace(); return false;}
    }
}
