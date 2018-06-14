package graphics.drawers;

import graphics.IFrameUpdatable;
import graphics.Layer;
import graphics.drawers.drawables.Drawable;
import javafx.scene.canvas.GraphicsContext;

import java.util.ArrayList;
import java.util.Arrays;

public class LayerDrawer extends Drawer implements IFrameUpdatable
{
    private ArrayList<Drawer> drawers;

    public LayerDrawer()
    {
        drawers = new ArrayList<>();
    }

    public LayerDrawer(Drawer... drawers)
    {
        this.drawers = new ArrayList<>(Arrays.asList(drawers));
    }

    public ArrayList<Drawer> getDrawers()
    {
        return drawers;
    }

    @Override
    public Drawable getDrawable()
    {
        if (drawers.size() > 0)
            return drawers.get(0).getDrawable();
        else
            return null;
    }

    @Override
    protected void onDraw(GraphicsContext gc)
    {
        for (Drawer drawer : drawers)
            drawer.draw(gc);
    }

    @Override
    public void update(double deltaT)
    {
        drawers.stream()
                .filter(d -> d instanceof IFrameUpdatable)
                .forEach(d -> ((IFrameUpdatable)d).update(deltaT));
    }
}
