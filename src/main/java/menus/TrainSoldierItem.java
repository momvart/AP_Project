package menus;

import models.soldiers.*;

public class TrainSoldierItem extends Menu
{
    private static final String sItemTextPattern = "%s %c x%d";

    private int soldierType;
    private int availableCount;

    public TrainSoldierItem(int soldierType, int availableCount)
    {
        super(Id.BARRACKS_TRAIN_ITEM,
                String.format(sItemTextPattern,
                        SoldierValues.getSoldierInfo(soldierType).getName(),
                        availableCount >= 0 ? 'A' : 'U',
                        availableCount));
        this.soldierType = soldierType;
        this.availableCount = availableCount;
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
