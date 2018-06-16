package graphics;

import graphics.drawers.Drawer;
import graphics.drawers.drawables.MenuItemDrawable;
import graphics.positioning.PercentPositioningSystem;
import javafx.scene.input.MouseEvent;
import menus.*;
import utils.RectF;

import java.util.ArrayList;

public class MenuLayer extends Layer
{
    private static final double ItemCellSize = 100;

    private ParentMenu currentMenu;
    private IMenuClickListener clickListener;

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
        removeAllObjects();

        ArrayList<Menu> items = currentMenu.getMenuItems();
        for (int i = 0; i < items.size(); i++)
        {
            Menu item = items.get(i);
            MenuItemDrawable drawable = new MenuItemDrawable(item, ItemCellSize, ItemCellSize);
            drawable.setPivot(0.5, 0.5);
            Drawer drawer = new Drawer(drawable);
            drawer.setPosition(1.0 / (items.size() + 1) * (i + 1), 0.5);
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
