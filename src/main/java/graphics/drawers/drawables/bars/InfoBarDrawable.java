package graphics.drawers.drawables.bars;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class InfoBarDrawable extends Bar
{
    int textLines;
    public static final double BOX_WIDTH_SCALE = 0.3;
    public static final double LINE_SPACING_SCALE = 0.05;

    public InfoBarDrawable(double width, double height, int textLines)
    {
        super(width, height);
        this.textLines = textLines;
    }

    @Override
    protected void onDraw(GraphicsContext gc)
    {
        gc.setFill(Color.rgb(0, 0, 0, 0.6));
        int lines = textLines;
        double boxWidth = BOX_WIDTH_SCALE * width;
        double boxHeight = lines * LINE_SPACING_SCALE * height;
        System.err.println(boxWidth);
        System.err.println(boxHeight);
        gc.fillRoundRect(width / 2 - boxWidth / 2, TopBarDrawable.HEIGHT_SCALE * height, boxWidth, boxHeight, 10, 10);
    }
}
