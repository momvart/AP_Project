package graphics.drawers.drawables.bars;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class BottomBarDrawable extends Bar
{
    private int itemCount;
    public static final double COLUMNS_SPACING_SCALE = 0.05;
    public static final double BOX_HEIGHT_SCALE = 0.10;

    public BottomBarDrawable(double width, double height, int text)
    {
        super(width, height);
        this.itemCount = text;
        setFill(Color.rgb(255, 255, 255, 0.6));
    }

    @Override
    protected void onDraw(GraphicsContext gc)
    {
        int columns = itemCount;
        double boxWidth = (columns) * COLUMNS_SPACING_SCALE * width;
        double boxHeight = BOX_HEIGHT_SCALE * height;

        gc.fillRoundRect(width / 2 - boxWidth / 2, height - boxHeight, boxWidth, boxHeight, 10, 10);
    }
}
