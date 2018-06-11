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
        this.img = img;
        this.setSize(width, height);
    }

    public ImageDrawable(Image img, double minSideDimen)
    {
        this.img = img;
        if (img.getWidth() <= img.getHeight())
            setSize(minSideDimen, img.getHeight() / img.getWidth() * minSideDimen);
        else
            setSize(img.getWidth() / img.getHeight() * minSideDimen, minSideDimen);
    }

    @Override
    public void onDraw(GraphicsContext gc)
    {
        if (img == null)
            return;
        gc.drawImage(img, 0, 0, getWidth(), getHeight());
    }
}
