package graphics.drawers.drawables;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public class HealthHProgressbarDrawable extends HProgressbarDrawable
{
    private static Color[] healthColors = new Color[] { Color.RED, Color.ORANGE, Color.YELLOW, Color.FORESTGREEN, Color.FORESTGREEN };


    public HealthHProgressbarDrawable(double width, double height, Paint fill)
    {
        super(width, height, fill);
    }

    @Override
    public void setProgress(double progress)
    {
        super.setProgress(progress);
        this.setFill(healthColors[(int)(getProgress() * (healthColors.length - 1))]);
    }
}
