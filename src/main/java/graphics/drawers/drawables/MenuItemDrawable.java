package graphics.drawers.drawables;

import graphics.Fonts;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import menus.Menu;
import utils.GraphicsUtilities;

public class MenuItemDrawable extends Drawable
{
    private static final String IconsPath = "assets/menu/icons/";
    private static final double IconHeightRatio = 2.0 / 3;

    private Menu menu;

    private RoundRectDrawable background;
    private ImageDrawable icon;
    private TextDrawable label;

    public MenuItemDrawable(Menu menu, double width, double height)
    {
        this.menu = menu;

        this.background = new RoundRectDrawable(width, height, 10, Color.rgb(0, 0, 0, 1));
        if (!menu.getIconPath().isEmpty())
            try
            {
                this.icon = GraphicsUtilities.createImageDrawable(IconsPath + menu.getIconPath(), width, height * IconHeightRatio, true);
                this.icon.setPivot(.5, .5);
            }
            catch (Exception ignored) {}
        else
            this.icon = new ImageDrawable(null);

        this.label = new TextDrawable(menu.getText().toUpperCase(), Color.WHITE, Fonts.getTiny());
        this.label.setPivot(.5, .5);

        setSize(width, height);
    }

    public Menu getMenu()
    {
        return menu;
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
