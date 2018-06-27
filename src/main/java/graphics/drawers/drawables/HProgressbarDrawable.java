package graphics.drawers.drawables;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Paint;

public class HProgressbarDrawable extends Drawable
{


    public static final int CornerRadius = 2;
    private Paint background;
    private Paint stroke;

    private double progress;

    private boolean rtl;

    public HProgressbarDrawable(double width, double height, Paint fill)
    {
        this(width, height, fill, false);
    }

    public HProgressbarDrawable(Paint fill)
    {
        this(100, 100, fill);
    }

    public HProgressbarDrawable(double width, double height, Paint fill, boolean rtl)
    {
        setSize(width, height);
        setFill(fill);
        this.rtl = rtl;
    }

    public void setBackground(Paint background)
    {
        this.background = background;
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

    public void setRightToLeft(boolean rtl)
    {
        this.rtl = rtl;
    }

    @Override
    protected void onDraw(GraphicsContext gc)
    {
        gc.setFill(background);
        gc.fillRoundRect(0, 0, getWidth(), getHeight(), CornerRadius, CornerRadius);
        gc.setFill(getFill());
        gc.fillRoundRect(!rtl ? 0 : getWidth() * (1 - progress), 0, getWidth() * progress, getHeight(), CornerRadius, CornerRadius);
        gc.setLineWidth(1);
        gc.setStroke(stroke);
        gc.strokeRoundRect(0, 0, getWidth(), getHeight(), CornerRadius, CornerRadius);
    }
}
