package menus;

import graphics.GraphicsValues;
import models.soldiers.SoldierValues;

public class TrainSoldierItem extends Menu
{
    private static final String sItemTextPattern = "%s %c x%d";

    private int soldierType;
    private int availableCount;

    public TrainSoldierItem(int soldierType, int availableCount)
    {
        super(Id.BARRACKS_TRAIN_ITEM, SoldierValues.getSoldierInfo(soldierType).getName());
        this.soldierType = soldierType;
        this.availableCount = availableCount;
        if (availableCount == 0 || availableCount == -1)
            setDisabled(true);

        this.setIconPath(GraphicsValues.getSoldierAssetsPath(soldierType) + "/icon/Icon" + (isDisabled() ? "G" : "") + ".png");
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
