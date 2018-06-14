package graphics.drawers.drawables;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public class RoundRectDrawable extends Drawable
{
    private double corner = 0;

    public RoundRectDrawable(double width, double height, double corner, Paint fill)
    {
        this.corner = corner;
        setSize(width, height);
        setFill(fill);
    }

    @Override
    protected void onDraw(GraphicsContext gc)
    {
        gc.fillRoundRect(0, 0, getWidth(), getHeight(), corner, corner);
    }
}
