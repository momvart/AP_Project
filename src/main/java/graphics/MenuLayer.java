package graphics;

import graphics.drawers.Drawer;
import graphics.drawers.drawables.Drawable;
import graphics.drawers.drawables.MenuItemDrawable;
import graphics.drawers.drawables.RoundRectDrawable;
import graphics.positioning.PercentPositioningSystem;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import menus.IMenuClickListener;
import menus.Menu;
import menus.ParentMenu;
import menus.Submenu;
import utils.RectF;

import java.util.ArrayList;

public class MenuLayer extends Layer
{
    private ParentMenu currentMenu;
    private IMenuClickListener clickListener;
    private static final double ItemCellSize = 50;
    private static final double ITEM_PADDING = 5;
    private static final double BOX_HEIGHT_SCALE = 0.1;

    public MenuLayer(int order, RectF bounds)
    {
        super(order, bounds);
        this.setPosSys(new PercentPositioningSystem(this));
    }

    public MenuLayer(int order, RectF bounds, IMenuClickListener listener)
    {
        super(order, bounds);
        this.setPosSys(new PercentPositioningSystem(this));
        this.clickListener = listener;
    }

    public ParentMenu getCurrentMenu()
    {
        return currentMenu;
    }

    public void setCurrentMenu(ParentMenu currentMenu)
    {
        this.currentMenu = currentMenu;
        updateMenu();
    }

    private void updateMenu()
    {
        ArrayList<Menu> items = currentMenu.getMenuItems();
        removeAllObjects();
        Drawable bg = new RoundRectDrawable((ItemCellSize + MenuLayer.ITEM_PADDING) * items.size() + ITEM_PADDING, ItemCellSize + 2 * ITEM_PADDING, 10, Color.rgb(0, 0, 0, 0.6));
        bg.setPivot(0.5, 1);
        Drawer bgDrawer = new Drawer(bg);
        bgDrawer.setPosition(0.5, 1);
        bgDrawer.setLayer(this);
        for (int i = 0; i < items.size(); i++)
        {
            Menu item = items.get(i);
            MenuItemDrawable drawable = new MenuItemDrawable(item, ItemCellSize, ItemCellSize);
            drawable.setPivot(0.5, 0.5);
            Drawer drawer = new Drawer(drawable);
            double x = 0.5 + (i - items.size() / 2.0 + 0.5) * (ITEM_PADDING + 0.5) / 100;
            drawer.setPosition(x, 1 - (ITEM_PADDING + 0.5) / 100);
            drawer.setClickListener(this::onMenuItemClick);
            drawer.setLayer(this);
        }
    }

    private void onMenuItemClick(Drawer drawer, MouseEvent event)
    {
        Menu item = ((MenuItemDrawable)drawer.getDrawable()).getMenu();
        if (item.isClickable())
            callOnMenuItemClick(item);
        if (item instanceof ParentMenu)
            setCurrentMenu((ParentMenu)item);
        else if (item.getId() == Menu.Id.BACK)
            setCurrentMenu(((Submenu)currentMenu).getParent());
    }


    private void callOnMenuItemClick(Menu item)
    {
        if (clickListener != null)
            clickListener.onItemClicked(item);
    }

    public void setClickListener(IMenuClickListener clickListener)
    {
        this.clickListener = clickListener;
    }
}
