package menus;

import exceptions.InvalidCommandException;

import java.util.ArrayList;

public class Submenu extends ParentMenu
{
    ParentMenu parent;

    public Submenu(int id, String text, ParentMenu parent)
    {
        super(id, text);
        this.parent = parent;
    }

    public ParentMenu getParent()
    {
        return parent;
    }

    public void back(IMenuContainer view)
    {
        view.setCurrentMenu(parent, true);
    }

    @Override
    public ArrayList<String> getItems()
    {
        ArrayList<String> retVal = super.getItems();
        retVal.add(String.format(Menu.sItemNamePattern, items.size() + 1, "back"));
        return retVal;
    }

    @Override
    public void handleCommand(String command, IMenuContainer container) throws InvalidCommandException
    {
        if (commandHandler instanceof MenuNumberCommandHandler && command.equals(Integer.toString(items.size() + 1))
                || commandHandler instanceof MenuTextCommandHandler && command.equalsIgnoreCase("back"))
            container.setCurrentMenu(parent, true);
        else
            super.handleCommand(command, container);
    }
}
