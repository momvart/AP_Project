package menus;

import exceptions.InvalidCommandException;

import java.util.ArrayList;

public class ParentMenu extends Menu
{
    protected ArrayList<Menu> items;
    protected IMenuCommandHandler commandHandler;

    public ParentMenu(int id, String text)
    {
        super(id, text);
        items = new ArrayList<>();
        commandHandler = MenuNumTextCommandHandler.getInstance();
    }

    public ParentMenu(int id, String text, IMenuCommandHandler commandHandler)
    {
        super(id, text);
        this.items = new ArrayList<>();
        this.commandHandler = commandHandler;
    }

    public ArrayList<String> getItems()
    {
        ArrayList<String> retVal = new ArrayList<>(items.size());
        for (int i = 0; i < items.size(); i++)
            retVal.add(String.format("%d. %s", i + 1, items.get(i).getText()));
        return retVal;
    }

    public ParentMenu insertItem(int id, String text)
    {
        return insertItem(new Menu(id, text));
    }

    public ParentMenu insertItem(Menu menu)
    {
        items.add(menu);
        return this;
    }

    public void handleCommand(String command, IMenuContainer container) throws InvalidCommandException
    {
        Menu menu = commandHandler.handle(items, command);
        if (menu == null)
            throw new InvalidCommandException(command);
        else if (menu instanceof ParentMenu)
            container.setCurrentMenu((ParentMenu)menu, true);
        else
            container.onMenuItemClicked(menu);
    }
}
