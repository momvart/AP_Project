package menus;

public interface IMenuContainer extends IMenuClickListener
{
    void setCurrentMenu(ParentMenu menu, boolean showNow);

    ParentMenu getCurrentMenu();

    void showCurrentMenu();

    void addClickListener(IMenuClickListener listener);
}
