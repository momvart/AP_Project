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

    private final double IconHeightRatio;

    public static final Color LIGHT = Color.rgb(255, 255, 255, 0.6);
    public static final Color DARK = GraphicsValues.BLACK_60;

    protected RoundRectDrawable background;
    protected RoundRectDrawable bgForeground;
    protected ImageDrawable icon = new ImageDrawable(null);
    protected TextDrawable label;

    public ButtonDrawable(String text, String iconPath, double width, double height)
    {
        this.background = new RoundRectDrawable(width, height, CornerRadius, DARK);
        this.bgForeground = new RoundRectDrawable(width - 2 * InsidePadding, height / 2 - InsidePadding, CornerRadius, Color.rgb(255, 255, 255, 0.2));

        this.label = new TextDrawable(text, Color.WHITE, Fonts.getSmaller());
        this.label.setPivot(.5, 1);
        this.label.setHasShadow(true);
        this.label.setMaxWidth(width - 2 * InsidePadding);

        if (!text.isEmpty())
            IconHeightRatio = 2 / 3.0;

        else
            IconHeightRatio = 1;

        if (!iconPath.isEmpty())
            try
            {
                this.icon = GraphicsUtilities.createImageDrawable(iconPath, width - 2 * InsidePadding, height * IconHeightRatio - 2 * InsidePadding, true);
                this.icon.setPivot(.5, .5);
            }
            catch (Exception ignored) { }

        setSize(width, height);
    }

    public void setText(String text)
    {
        this.label.setText(text);
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
        gc.translate(getWidth() / 2, getHeight() * IconHeightRatio / 2);
        icon.draw(gc);
        gc.restore();

        if (label != null)
        {
            gc.save();
            gc.translate(getWidth() / 2, getHeight() - InsidePadding);
            label.draw(gc);
            gc.restore();
        }
    }
}
