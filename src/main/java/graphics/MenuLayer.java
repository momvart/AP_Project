package graphics;

import graphics.drawers.Drawer;
import graphics.drawers.drawables.MenuItemDrawable;
import graphics.positioning.NormalPositioningSystem;
import javafx.scene.input.MouseEvent;
import menus.*;
import utils.RectF;

import java.util.ArrayList;

public class MenuLayer extends Layer
{
    private double itemCellSize;

    private static final double ItemPadding = 10;

    private ParentMenu currentMenu;
    private IMenuClickListener clickListener;

    public enum Orientation
    {
        VERTICAL,
        HORIZONTAL
    }

    private Orientation orientation;

    public MenuLayer(int order, RectF bounds, Orientation orientation)
    {
        super(order, bounds);
        this.setPosSys(new NormalPositioningSystem(1));
        this.orientation = orientation;
        itemCellSize = (orientation == Orientation.VERTICAL ? bounds.getWidth() : bounds.getHeight()) - 2 * ItemPadding;
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

    public void setItemCellSize(double itemCellSize)
    {
        this.itemCellSize = itemCellSize;
    }

    private void updateMenu()
    {
        removeAllObjects();

        ArrayList<Menu> items = currentMenu.getMenuItems();

        final double start = ((orientation == Orientation.VERTICAL ? getBounds().getHeight() : getBounds().getWidth())
                - (itemCellSize + ItemPadding) * items.size()) / 2;


        for (int i = 0; i < items.size(); i++)
        {
            Menu item = items.get(i);
            MenuItemDrawable drawable = new MenuItemDrawable(item, itemCellSize, itemCellSize);
            drawable.setPivot(orientation == Orientation.HORIZONTAL ? 0 : 0.5, orientation == Orientation.VERTICAL ? 0 : 0.5);
            Drawer drawer = new Drawer(drawable);
            if (orientation == Orientation.HORIZONTAL)
                drawer.setPosition(start + (itemCellSize + ItemPadding) * i, getBounds().getHeight() / 2);
            else
                drawer.setPosition(getBounds().getWidth() / 2, start + (itemCellSize + ItemPadding) * i);
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