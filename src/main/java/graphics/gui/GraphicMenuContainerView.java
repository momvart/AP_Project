package graphics.gui;

import exceptions.InvalidCommandException;
import graphics.Layer;
import menus.IMenuClickListener;
import menus.IMenuContainer;
import menus.Menu;
import menus.ParentMenu;
import utils.ICommandManager;

import java.util.ArrayList;

public class GraphicMenuContainerView extends GraphicView implements IMenuContainer, ICommandManager
{
    protected ParentMenu currentMenu;
    protected ArrayList<IMenuClickListener> listeners = new ArrayList<>();
    protected GraphicView villageView;

    public GraphicMenuContainerView(Layer layer, double width, double height)
    {
        super(layer, width, height);
    }

    @Override
    public void onItemClicked(Menu menu)
    {
        listeners.forEach(iMenuClickListener -> iMenuClickListener.onItemClicked(menu));
    }

    @Override
    public void setCurrentMenu(ParentMenu menu, boolean showNow)
    {
        currentMenu = menu;
        if (showNow)
            showCurrentMenu();
    }

    @Override
    public ParentMenu getCurrentMenu()
    {
        return currentMenu;
    }

    @Override
    public void showCurrentMenu()
    {
        villageView.showBottomBar(currentMenu);
    }

    @Override
    public void addClickListener(IMenuClickListener listener) { listeners.add(listener); }

    @Override
    public void manageCommand(String command)
    {
        try
        {
            currentMenu.handleCommand(command, this);
        }
        catch (InvalidCommandException e)
        {
            e.printStackTrace();
        }
    }
}
