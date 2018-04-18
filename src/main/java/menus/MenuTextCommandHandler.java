package menus;

import java.util.ArrayList;

public class MenuTextCommandHandler implements IMenuCommandHandler
{
    private static MenuTextCommandHandler instance;

    public static MenuTextCommandHandler getInstance()
    {
        if (instance == null)
            instance = new MenuTextCommandHandler();
        return instance;
    }

    @Override
    public Menu handle(ArrayList<Menu> items, String command)
    {
        return items.stream().filter(menu -> menu.getText().equalsIgnoreCase(command)).findFirst().orElse(null);
    }
}
