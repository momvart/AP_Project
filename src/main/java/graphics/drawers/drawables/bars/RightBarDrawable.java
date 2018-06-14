package graphics.drawers.drawables.bars;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class RightBarDrawable extends Bar
{
    private int soldiersTypeCount;

    public RightBarDrawable(double width, double height, int soldiersTypeCount)
    {
        super(width, height);
        this.soldiersTypeCount = soldiersTypeCount;
    }

    @Override
    protected void onDraw(GraphicsContext gc)
    {
        double boxWidth = 0.04 * width;
        gc.setFill(Color.rgb(0, 0, 0, 0.6));
        double boxHeight;
        boxHeight = soldiersTypeCount * height / 15;
        gc.fillRoundRect(width - boxWidth, height / 2 - boxHeight / 2, boxWidth, boxHeight, 10, 10);
    }
}
