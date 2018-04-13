package views;

import menus.*;

import java.util.ArrayList;
import java.util.Scanner;

public abstract class ConsoleMenuContainerView extends ConsoleView implements IMenuContainer
{
    protected ParentMenu currentMenu;
    protected ArrayList<IMenuClickListener> listeners = new ArrayList<>();

    public ConsoleMenuContainerView(Scanner scanner)
    {
        super(scanner);
    }

    @Override
    public void onMenuItemClicked(Menu menu)
    {
        listeners.forEach(listener -> listener.onItemClicked(menu));
    }

    @Override
    public void setCurrentMenu(ParentMenu menu, boolean showNow)
    {
        if (showNow)
            showCurrentMenu();
        currentMenu = menu;
    }

    @Override
    public void showCurrentMenu()
    {
        currentMenu.getItems().forEach(System.out::println);
    }

    @Override
    public void addClickListener(IMenuClickListener listener)
    {
        listeners.add(listener);
    }
}
