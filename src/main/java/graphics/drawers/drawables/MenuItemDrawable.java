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
        background.setEnableDashes(true);
    }

    public Menu getMenu()
    {
        return menu;
    }

    @Override
    protected void onPreDraw(GraphicsContext gc)
    {
        if (menu.isDisabled())
            setFill(Color.rgb(200, 200, 200, 0.6));
        else
            setFill(Color.rgb(0, 0, 0, 0.6));

        if (menu.isFocused())
            background.setStroke(Color.WHITE);
        else
            background.setStroke(Color.TRANSPARENT);


        super.onPreDraw(gc);
    }
}
