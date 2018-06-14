package graphics.drawers.drawables;

import graphics.Fonts;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import menus.Menu;
import utils.GraphicsUtilities;

import java.net.URISyntaxException;

public class MenuItemDrawable extends Drawable
{
    private static final double IconHeightRatio = 2.0 / 3;

    private Menu menu;

    private RoundRectDrawable background;
    private ImageDrawable icon;
    private TextDrawable label;

    public MenuItemDrawable(Menu menu, double width, double height)
    {
        this.menu = menu;

        this.background = new RoundRectDrawable(width, height, 10, Color.rgb(0, 0, 0, 0.6));
//        this.icon = new ImageDrawable(icon, width, height * IconHeightRatio);
        try
        {
            this.icon = GraphicsUtilities.createImageDrawable("assets/buildings/gold mine/001/001.png", width, height * IconHeightRatio);
        }
        catch (URISyntaxException e)
        {
            e.printStackTrace();
        }
        this.icon.setPivot(.5, .5);
        this.label = new TextDrawable(menu.getText(), Fonts.getMedium());
        this.label.setPivot(.5, .5);

        setSize(width, height);
    }

    @Override
    protected void onDraw(GraphicsContext gc)
    {
        background.draw(gc);

        gc.save();
        gc.translate(getWidth() / 2, getHeight() * IconHeightRatio / 2);
        icon.draw(gc);
        gc.restore();

        gc.save();
        gc.translate(getWidth() / 2, getHeight() - getHeight() * (1 - IconHeightRatio) * .5);
        label.draw(gc);
        gc.restore();
    }
}
