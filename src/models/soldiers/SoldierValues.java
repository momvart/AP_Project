package models.soldiers;

import java.util.ArrayList;

public abstract class SoldierValues
{
    private static ArrayList<SoldierInfo> infos;

    public static void initialize()
    {

    }

    public static SoldierInfo getSoldierInfo(int type)
    {
        return infos.get(type - 1);
    }
}
