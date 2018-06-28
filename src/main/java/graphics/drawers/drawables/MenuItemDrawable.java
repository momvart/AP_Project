package graphics.drawers.drawables;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import menus.Menu;

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

    @Override
    protected void onPreDraw(GraphicsContext gc)
    {
        if (menu.isFocused())
            setFill(Color.rgb(128, 128, 128, 0.6));
        else
            setFill(Color.rgb(0, 0, 0, 0.6));
        super.onPreDraw(gc);
    }
}
