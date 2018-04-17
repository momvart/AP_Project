package views;

import exceptions.InvalidCommandException;
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

    private boolean getCancelled = false;

    public void startGetting()
    {
        getCancelled = false;
        while (!getCancelled)
        {
            String command = getCommand();
            try
            {
                currentMenu.handleCommand(command, this);
            }
            catch (InvalidCommandException ex)
            {
                showError(ex);
            }
        }
    }

    public void stopGetting()
    {
        getCancelled = true;
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
