package models.soldiers;

import java.util.ArrayList;

public abstract class SoldierValues
{
    public static final int SOLDIER_TYPES_COUNT = 6;

    private static ArrayList<SoldierInfo> infos;

    public static void initialize()
    {

    }

    public static SoldierInfo getSoldierInfo(int type)
    {
        return infos.get(type - 1);
    }

    public static ArrayList<SoldierInfo> getInfos() { return infos; }
}
