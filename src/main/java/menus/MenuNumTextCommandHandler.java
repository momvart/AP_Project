package menus;

import java.util.ArrayList;

public class MenuNumTextCommandHandler implements IMenuCommandHandler
{
    private static MenuNumTextCommandHandler instance;

    public static MenuNumTextCommandHandler getInstance()
    {
        if (instance == null)
            instance = new MenuNumTextCommandHandler();
        return instance;
    }

    private MenuTextCommandHandler textHandler = MenuTextCommandHandler.getInstance();
    private MenuNumberCommandHandler numberHandler = MenuNumberCommandHandler.getInstance();

    @Override
    public Menu handle(ArrayList<Menu> items, String command)
    {
        if (command.matches("\\d+"))
            return numberHandler.handle(items, command);
        else
            return textHandler.handle(items, command);
    }
}
