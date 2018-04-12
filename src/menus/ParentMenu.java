package menus;

import java.util.ArrayList;

public class ParentMenu extends Menu
{
    ArrayList<Menu> items;

    public ParentMenu(int id, String text)
    {
        super(id, text);
        items = new ArrayList<>();
    }

    public ArrayList<String> getItems()
    {
        ArrayList<String> retVal = new ArrayList<>(items.size());
        for (int i = 0; i < items.size(); i++)
            retVal.add(String.format("%d. %s", i, items.get(i)));
        return retVal;
    }

    public ParentMenu insertItem(int id , String text)
    {
        return insertItem( new Menu(id, text));
    }

    public ParentMenu insertItem(Menu menu)
    {
        items.add(menu);
        return this;
    }

    public void handleCommand(String command, IMenuContainer container)
    {

    }
}
