package graphics.drawers.drawables;

import graphics.Fonts;
import graphics.GraphicsValues;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import menus.Menu;
import utils.GraphicsUtilities;

public class MenuItemDrawable extends ButtonDrawable
{
    private Menu menu;

    public MenuItemDrawable(Menu menu, double width, double height)
    {
        super(menu.getText().toUpperCase(), menu.getIconPath(), width, height);
        this.menu = menu;
    }

    public Menu getMenu()
    {
        return menu;
    }

}
