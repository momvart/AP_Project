package graphics;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import utils.RectF;

import java.util.ArrayList;

public class GraphicHandler implements IFrameUpdatable
{
    private GraphicsContext gc;
    private GameScene scene;

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
        gc.transform(scale, 0, 0, scale, 0, 0);
        gc.save();
        this.scale = scale;
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


    public void addUpdatable(IFrameUpdatable updatable)
    {
        updatables.add(updatable);
    }

    @Override
    public void update(double deltaT)
    {
        updatables.forEach(u -> u.update(deltaT));
        scene.update(deltaT);
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
}
