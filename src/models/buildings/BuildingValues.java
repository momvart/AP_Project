package models.buildings;

import java.util.ArrayList;

public class BuildingValues
{
    private static ArrayList<BuildingInfo> infos;

    public static void initialize()
    {
        //TODO: read from file or set infos
    }

    public static BuildingInfo getBuildingInfo(int type)
    {
        return infos.get(type - 1);
    }
}
