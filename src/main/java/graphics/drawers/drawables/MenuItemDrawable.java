package graphics.drawers.drawables;

import graphics.Fonts;
import graphics.GraphicsValues;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import menus.Menu;
import utils.GraphicsUtilities;

public class MenuItemDrawable extends Drawable
{
    private static final double InsidePadding = GraphicsValues.PADDING / 2;

    private static final String IconsPath = "assets/menu/icons/";
    private static final double IconHeightRatio = 2.0 / 3;

    private Menu menu;

    private RoundRectDrawable background;
    private ImageDrawable icon;
    private TextDrawable label;

    public MenuItemDrawable(Menu menu, double width, double height)
    {
        this.menu = menu;

        this.background = new RoundRectDrawable(width, height, 10, Color.rgb(0, 0, 0, 0.6));
        if (!menu.getIconPath().isEmpty())
            try
            {
                this.icon = GraphicsUtilities.createImageDrawable(menu.getIconPath(), width, height * IconHeightRatio, true);
                this.icon.setPivot(.5, .5);
            }
            catch (Exception ignored)
            {
                ignored.printStackTrace(System.out);
                System.out.println(menu.getIconPath());
            }
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
        gc.translate(getWidth() / 2, getHeight() * IconHeightRatio / 2 + InsidePadding);
        icon.draw(gc);
        gc.restore();

        gc.save();
        gc.translate(getWidth() / 2, getHeight() - getHeight() * (1 - IconHeightRatio) * .5 + InsidePadding * 2);
        label.draw(gc);
        gc.restore();
    }
}
