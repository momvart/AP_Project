package graphics.drawers.drawables.bars;

import graphics.drawers.drawables.Drawable;
import graphics.drawers.drawables.ImageDrawable;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import menus.ParentMenu;

public class MenuDrawable extends Bar
{
    ParentMenu menu;
    Drawable[] items;

    public double getScreenWidth()
    {
        return width;
    }

    public double getScreenHeight()
    {
        return height;
    }

    public MenuDrawable(double width, double height, ParentMenu menu)
    {
        super(width, height);
        this.menu = menu;
    }

    public ParentMenu getMenu()
    {
        return menu;
    }

    public void setMenu(ParentMenu menu)
    {
        this.menu = menu;
    }

    public Drawable[] getItems()
    {
        return items;
    }

    @Override
    protected void onDraw(GraphicsContext gc)
    {
        double tileSize = height / 10;
        int size = menu.getItems().size();
        Drawable[] items = new Drawable[size];
        for (int i = 0; i < items.length; i++)
        {
            items[i] = new ImageDrawable(new Image("assets/soldiers/guardian/5/idle/001.png"), tileSize, tileSize);
            items[i].setPivot(0.5, 0.5);
        }
        this.items = items;
    }
}
