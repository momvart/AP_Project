package menus;

import graphics.GraphicsValues;
import graphics.drawers.SoldierDrawer;
import models.soldiers.*;

public class TrainSoldierItem extends Menu
{
    private static final String sItemTextPattern = "%s %c x%d";

    private int soldierType;
    private int availableCount;

    public TrainSoldierItem(int soldierType, int availableCount)
    {
        super(Id.BARRACKS_TRAIN_ITEM,
                SoldierValues.getSoldierInfo(soldierType).getName() + " " +
                        (availableCount >= 0 ? "A x" + availableCount : "U"));
        this.soldierType = soldierType;
        this.availableCount = availableCount;
        this.setIconPath(GraphicsValues.getSoldierAssetsPath(soldierType) + "/Icon.png");
    }

    public int getSoldierType()
    {
        return soldierType;
    }

    public int getAvailableCount()
    {
        return availableCount;
    }
}
