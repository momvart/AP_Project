package menus;

import graphics.GraphicsValues;

public class SoldierMenuItem extends Menu
{
    private int count;
    private int soldierType;

    public SoldierMenuItem(int id, String text, int count, int soldierType)
    {
        super(id, text);

        this.soldierType = soldierType;
        setFocusable(true);
        setCount(count);
    }

    public void setCount(int count)
    {
        this.count = count;

        setDisabled(count == 0);
    }

    @Override
    public void setDisabled(boolean disabled)
    {
        super.setDisabled(disabled);

        if (disabled)
            setIconPath(GraphicsValues.getSoldierAssetsPath(soldierType) + "/icon/IconG.png");
        else
            setIconPath(GraphicsValues.getSoldierAssetsPath(soldierType) + "/icon/Icon.png");
    }

    @Override
    public String getText()
    {
        return super.getText() + " x" + count;
    }
}
