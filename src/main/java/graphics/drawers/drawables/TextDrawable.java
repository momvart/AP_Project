package graphics.drawers.drawables;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.*;
import javafx.scene.text.*;
import utils.*;

public class TextDrawable extends Drawable
{
    private Text privateText;
    private String text;

    private Font font;

    public TextDrawable(String text, Font font)
    {
        this(text, Color.BLACK, font);
    }

    public TextDrawable(String text, Paint fill, Font font)
    {
        this.text = text;
        this.font = font;
        setFill(fill);

        privateText = new Text(text);
        updatePrivateText();
    }

    public String getText()
    {
        return text;
    }

    public void setText(String text)
    {
        if (this.text.equals(text))
            return;
        this.text = text;
        updatePrivateText();
    }

    public void setFont(Font font)
    {
        this.font = font;
        updatePrivateText();
    }

    @Override
    public SizeF getSize()
    {
        return new SizeF(privateText.getBoundsInLocal().getWidth(), privateText.getBoundsInLocal().getHeight());
    }

    private void updatePrivateText()
    {
        privateText.setText(text);
        privateText.setFont(font);
        privateText.applyCss();
        privateText.setLineSpacing(0);
        setPivots();
    }

    @Override
    protected void onPreDraw(GraphicsContext gc)
    {
        super.onPreDraw(gc);
        gc.save();
        gc.setFont(font);
        gc.translate(0, privateText.getBaselineOffset());
    }

    @Override
    protected void onDraw(GraphicsContext gc)
    {
        gc.fillText(text, 0, 0);
    }

    @Override
    protected void onPostDraw(GraphicsContext gc)
    {
        gc.restore();
        super.onPostDraw(gc);
    }
}
