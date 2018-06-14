package graphics.drawers.drawables;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Paint;

public class VProgressbarDrawable extends Drawable
{
    private Paint stroke;

    private double progress;

    public VProgressbarDrawable(Paint fill)
    {
        setFill(fill);
    }


    public void setStroke(Paint stroke)
    {
        this.stroke = stroke;
    }

    public double getProgress()
    {
        return progress;
    }

    /**
     * @param progress Must be between 0 and 1
     */
    public void setProgress(double progress)
    {
        this.progress = progress;
    }

    @Override
    protected void onDraw(GraphicsContext gc)
    {
        gc.fillRect(0, 0, getWidth() * progress, getHeight());
        gc.setStroke(stroke);
        gc.strokeRoundRect(0, 0, getWidth(), getHeight(), 2, 2);
    }
}
