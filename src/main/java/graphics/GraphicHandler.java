package graphics;

import javafx.scene.canvas.GraphicsContext;
import utils.RectF;

public class GraphicHandler implements IFrameUpdatable
{
    private GraphicsContext gc;
    private GameScene scene;

    public GraphicHandler(GraphicsContext gc, RectF camera)
    {
        this.gc = gc;
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

    @Override
    public void update(double deltaT)
    {
        scene.update(deltaT);
        scene.draw(gc, cameraBounds);
    }
}
