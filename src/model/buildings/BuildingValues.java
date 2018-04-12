package model.buildings;

public class BuildingValues
{
    private  static BuildingInfo[] infos;

    public static void initialize()
    {
        //TODO: read from file or set infos
    }

    public static BuildingInfo getInfo(int type)
    {
        return infos[type - 1];
    }
}
