package graphics.drawers.drawables;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Paint;


public class CircleDrawable extends Drawable
{
    private double radius;

    public CircleDrawable(double radius, Paint fill)
    {
        setSize(2 * radius, 2 * radius);
        setFill(fill);
        this.radius = radius;
    }

    @Override
    protected void onDraw(GraphicsContext gc)
    {
        gc.fillOval(0, 0, 2 * radius, 2 * radius);
    }
}
