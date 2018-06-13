package graphics;

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
        this.gc.setTransform(GraphicsValues.getScale(), 0, 0, GraphicsValues.getScale(), 0, 0);
        updateCamera(camera);
    }

    public void setScene(GameScene scene)
    {
        this.scene = scene;
    }

    private RectF cameraBounds = new RectF(0, 0, 0, 0);

    public void updateCamera(RectF newBounds)
    {
        gc.translate(cameraBounds.getX() - newBounds.getX(), cameraBounds.getY() - newBounds.getY());
        cameraBounds = newBounds;
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
        scene.handleMouseClick(event.getX() + cameraBounds.getX(), event.getY() + cameraBounds.getY(), event);
    }
}
