package menus;

import java.util.ArrayList;

public class MenuNumberCommandHandler implements IMenuCommandHandler
{
    @Override
    public Menu handle(ArrayList<Menu> items, String command)
    {
        try
        {
            return items.get(Integer.parseInt(command) - 1);
        }
        catch (Exception ex)
        {
            return null;
        }
    }
}
