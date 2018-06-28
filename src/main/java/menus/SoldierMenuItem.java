package menus;

public class SoldierMenuItem extends Menu
{
    private int count;
    private int soldierType;

    public SoldierMenuItem(int id, String text, int count, int soldierType)
    {
        super(id, text);
        this.count = count;
        this.soldierType = soldierType;
        setFocusable(true);
    }

    public SoldierMenuItem(int id, String text, String iconPath, int count, int soldierType)
    {
        super(id, text, iconPath);
        this.count = count;
        this.soldierType = soldierType;
    }

    @Override
    public String getText()
    {
        return super.getText() + " x" + count;
    }
}
