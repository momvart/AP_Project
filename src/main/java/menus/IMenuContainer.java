package menus;

public interface IMenuContainer
{
    void onMenuItemClicked(Menu menu);

    void setCurrentMenu(ParentMenu menu, boolean showNow);

    ParentMenu getCurrentMenu();

    void showCurrentMenu();

    void addClickListener(IMenuClickListener listener);
}
