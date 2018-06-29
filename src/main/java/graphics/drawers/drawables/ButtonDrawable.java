package graphics.drawers.drawables;

import graphics.Fonts;
import graphics.GraphicsValues;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import utils.GraphicsUtilities;

public class ButtonDrawable extends Drawable
{
    private static final double CornerRadius = 10;
    private static final double InsidePadding = GraphicsValues.PADDING / 2;

    private static final double IconHeightRatio = 2.0 / 3;

    protected RoundRectDrawable background;
    private RoundRectDrawable bgForeground;
    private ImageDrawable icon = new ImageDrawable(null);
    private TextDrawable label;

    public ButtonDrawable(String text, String iconPath, double width, double height)
    {
        this.background = new RoundRectDrawable(width, height, CornerRadius, Color.rgb(0, 0, 0, 0.6));
        this.bgForeground = new RoundRectDrawable(width - 2 * InsidePadding, height / 2 - InsidePadding, CornerRadius, Color.rgb(255, 255, 255, 0.2));
        if (!iconPath.isEmpty())
            try
            {
                this.icon = GraphicsUtilities.createImageDrawable(iconPath, width - 2 * InsidePadding, height * IconHeightRatio - 2 * InsidePadding, true);
                this.icon.setPivot(.5, .5);
            }
            catch (Exception ignored) { }


        this.label = new TextDrawable(text, Color.WHITE, Fonts.getSmaller());
        this.label.setPivot(.5, 1);

        setSize(width, height);
    }

    @Override
    public void setFill(Paint fill)
    {
        super.setFill(fill);
        background.setFill(fill);
    }

    @Override
    protected void onDraw(GraphicsContext gc)
    {
        background.draw(gc);

        gc.save();
        gc.translate(InsidePadding, InsidePadding);
        bgForeground.draw(gc);
        gc.restore();

        gc.save();
        gc.translate(getWidth() / 2, getHeight() * IconHeightRatio / 2 + InsidePadding);
        icon.draw(gc);
        gc.restore();

        gc.save();
        gc.translate(getWidth() / 2, getHeight() - InsidePadding);
        label.draw(gc);
        gc.restore();
    }
}
