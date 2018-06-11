package graphics.drawers.drawables;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.*;
import javafx.scene.text.*;
import utils.*;

public class TextDrawable extends Drawable
{
    private String text;

    private Font font;
    private Paint fill;

    public TextDrawable(String text, Font font)
    {
        this(text, Color.BLACK, font);
    }

    public TextDrawable(String text, Paint fill, Font font)
    {
        this.text = text;
        this.fill = fill;
        this.font = font;
    }

    @Override
    public void setPivot(double x, double y)
    {
        throw new UnsupportedOperationException();
    }

    public void setFill(Paint fill)
    {
        this.fill = fill;
    }

    public void setFont(Font font)
    {
        this.font = font;
    }

    @Override
    protected void onPreDraw(GraphicsContext gc)
    {
        super.onPreDraw(gc);
        gc.setFont(font);
        gc.setFill(fill);
    }

    @Override
    protected void onDraw(GraphicsContext gc)
    {
        gc.fillText(text, 0, 0);
    }
}
