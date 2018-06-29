package graphics.drawers.drawables;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public class RoundRectDrawable extends Drawable
{
    private double corner = 0;

    private Paint stroke = Color.TRANSPARENT;

    private boolean enableDashes = false;

    public RoundRectDrawable(double width, double height, double corner, Paint fill)
    {
        this.corner = corner;
        setSize(width, height);
        setFill(fill);
    }

    public void setStroke(Paint stroke)
    {
        this.stroke = stroke;
    }

    public void setEnableDashes(boolean enableDashes)
    {
        this.enableDashes = enableDashes;
    }

    @Override
    protected void onPreDraw(GraphicsContext gc)
    {
        super.onPreDraw(gc);
        gc.setLineWidth(2);
        if (enableDashes)
            gc.setLineDashes(5, 5);
        gc.setStroke(stroke);
    }

    @Override
    protected void onDraw(GraphicsContext gc)
    {
        gc.fillRoundRect(0, 0, getWidth(), getHeight(), corner, corner);
        gc.strokeRoundRect(0, 0, getWidth(), getHeight(), corner, corner);
    }
}
