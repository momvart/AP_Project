package graphics.layers;

import graphics.Fonts;
import graphics.GraphicsValues;
import graphics.drawers.Drawer;
import graphics.drawers.drawables.MenuItemDrawable;
import graphics.drawers.drawables.TextDrawable;
import graphics.positioning.NormalPositioningSystem;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import menus.IMenuClickListener;
import menus.Menu;
import menus.ParentMenu;
import menus.Submenu;
import utils.RectF;

import java.util.ArrayList;

public class MenuLayer extends Layer
{
    private double itemCellSize;

    private static final double ItemPadding = GraphicsValues.PADDING;

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

    public void updateMenu()
    {
        removeAllObjects();

        if (currentMenu == null)
            return;
        String title;
        String currentTitle = currentMenu.getText();
        String parentTitle = null;
        String parparentTitle = null;
        try
        {
            parentTitle = ((Submenu)currentMenu).getParent().getText();
            parparentTitle = ((Submenu)((Submenu)currentMenu).getParent()).getParent().getText();
        }
        catch (ClassCastException ignored)
        {
        }

        if (parparentTitle != null && !parparentTitle.equals(""))
            title = parparentTitle;
        else if (parentTitle != null && !parentTitle.equals(""))
            title = parentTitle;
        else
            title = currentTitle;
        TextDrawable txtBuilding = new TextDrawable(title, Color.web("#ffffa8"), new Font(Fonts.getLarge().getName(), Fonts.getLarge().getSize()));
        txtBuilding.setPivot(0.5, 1);
        txtBuilding.setHasShadow(true);
        Drawer tDrawer = new Drawer(txtBuilding);
        tDrawer.setPosition(getBounds().getWidth() / 2, 0);
        tDrawer.setLayer(this);
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

        setScroll(0, 0);
        setHorizontallyScrollable(items.size() * (itemCellSize + ItemPadding) > getWidth());
    }

    private void onMenuItemClick(Drawer drawer, MouseEvent event)
    {
        Menu item = ((MenuItemDrawable)drawer.getDrawable()).getMenu();
        if (item.isDisabled())
            return;
        if (item.isClickable())
            callOnMenuItemClick(item);
        if (item instanceof ParentMenu)
            setCurrentMenu((ParentMenu)item);
        else if (item.getId() == Menu.Id.BACK)
            setCurrentMenu(((Submenu)currentMenu).getParent());

        currentMenu.getMenuItems().stream().filter(Menu::isFocused).findFirst().ifPresent(i -> i.setFocused(false));
        item.setFocused(true);
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