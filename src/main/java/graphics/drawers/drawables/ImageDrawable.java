package graphics.drawers.drawables;

import graphics.GraphicsValues;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import utils.SizeF;

public class ImageDrawable extends Drawable
{
    private final Image img;

    public ImageDrawable(Image img)
    {
        this.img = img;
        if (img != null)
            this.setSize(img.getWidth(), img.getHeight());
    }

    public ImageDrawable(Image img, double width, double height)
    {
        this(img, width, height, true);
    }

    public ImageDrawable(Image img, double width, double height, boolean preserveRatio)
    {
        this(img, width, height, preserveRatio, false);
    }

    public ImageDrawable(Image img, double width, double height, boolean preserveRatio, boolean inside)
    {
        this.img = img;
        if (img == null)
            return;
        if (width == -1 && height == -1)
        {
            setSize(img.getWidth(), img.getHeight());
            return;
        }
        if (preserveRatio)
        {
            double ratio = img.getWidth() / img.getHeight();
            if (inside)
                if (ratio > width / height)
                    setSize(width, width / ratio);
                else
                    setSize(ratio * height, height);
            else
            {
                if (ratio < width / height)
                    setSize(width, width / ratio);
                else
                    setSize(ratio * height, height);
            }
        }
        else
            setSize(width, height);
    }

    public ImageDrawable(Image img, double minSideDimen)
    {
        this(img, minSideDimen, minSideDimen);
    }

    @Override
    public void onDraw(GraphicsContext gc)
    {
        if (img == null)
            return;
        gc.drawImage(img, 0, 0, getWidth(), getHeight());
    }
}
