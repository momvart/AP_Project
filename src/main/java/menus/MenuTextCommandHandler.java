package menus;

import java.util.ArrayList;

public class MenuTextCommandHandler implements IMenuCommandHandler
{
    @Override
    public Menu handle(ArrayList<Menu> items, String command)
    {
        return items.stream().filter(menu -> menu.getText().equalsIgnoreCase(command)).findFirst().orElse(null);
    }
}
