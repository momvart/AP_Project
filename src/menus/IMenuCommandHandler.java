package menus;

import java.util.ArrayList;

public interface IMenuCommandHandler
{
    Menu handle(ArrayList<Menu> items, String command);
}
