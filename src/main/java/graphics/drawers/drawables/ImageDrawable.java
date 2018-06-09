package graphics.drawers.drawables;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import utils.SizeF;

public class ImageDrawable extends Drawable
{
    private Image img;

    public ImageDrawable(Image img)
    {
        this.img = img;
        if (img != null)
            this.setSize((float)img.getWidth(), (float)img.getHeight());
    }

    public ImageDrawable(Image img, float width, float height)
    {
        this.img = img;
        this.setSize(width, height);
    }

    @Override
    public void onDraw(GraphicsContext gc)
    {
        if (img == null)
            return;
        //gc.save();
        //gc.translate(-getPivot().getX() * getSize().getWidth(), -getPivot().getY() * getSize().getHeight());
        gc.drawImage(img, 0, 0, getSize().getWidth(), getSize().getHeight());
        //gc.restore();
    }
}
