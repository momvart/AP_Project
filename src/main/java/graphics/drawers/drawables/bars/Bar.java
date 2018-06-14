package graphics.drawers.drawables.bars;

import graphics.drawers.drawables.Drawable;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public abstract class Bar extends Drawable
{
    protected double width;
    protected double height;

    private Paint fill = Color.rgb(0, 0, 0, 0.6);

    public Bar(double width, double height)
    {
        this.width = width;
        this.height = height;
    }

    public void setFill(Paint fill)
    {
        this.fill = fill;
    }

    @Override
    protected void onPreDraw(GraphicsContext gc)
    {
        super.onPreDraw(gc);
        gc.setFill(fill);
    }
}
