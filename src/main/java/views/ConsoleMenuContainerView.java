package views;

import exceptions.InvalidCommandException;
import menus.*;
import utils.ICommandManager;

import java.util.ArrayList;
import java.util.Scanner;

public abstract class ConsoleMenuContainerView extends ConsoleView implements IMenuContainer, ICommandManager
{
    protected ParentMenu currentMenu;
    protected ArrayList<IMenuClickListener> listeners = new ArrayList<>();

    public ConsoleMenuContainerView(Scanner scanner)
    {
        super(scanner);
    }

    @Override
    public void manageCommand(String command) throws InvalidCommandException
    {
        if (command.equalsIgnoreCase("showmenu"))
            showCurrentMenu();
        else if (currentMenu != null)
            currentMenu.handleCommand(command, this);
        else
            throw new InvalidCommandException(command);
    }

    @Override
    public void onMenuItemClicked(Menu menu)
    {
        listeners.forEach(listener -> listener.onItemClicked(menu));
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
        currentMenu.getItems().forEach(System.out::println);
    }

    @Override
    public void addClickListener(IMenuClickListener listener)
    {
        listeners.add(listener);
    }
}
