package graphics.drawers.drawables.bars;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class LeftBarDrawable extends Bar
{
    private int buildingTypesCount;

    public LeftBarDrawable(double width, double height, int buildingTypesCount)
    {
        super(width, height);
        this.buildingTypesCount = buildingTypesCount;
    }

    @Override
    protected void onDraw(GraphicsContext gc)
    {
        double boxWidth = 0.04 * width;
        gc.setFill(Color.rgb(0, 0, 0, 0.6));
        double boxHeight;
        if (buildingTypesCount <= 15)
            boxHeight = buildingTypesCount * height / 15;
        else
            boxHeight = height;
        gc.fillRoundRect(0, height / 2 - boxHeight / 2, boxWidth, boxHeight, 10, 10);
    }
}
