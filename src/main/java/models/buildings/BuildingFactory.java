package models.buildings;

import models.Map;
import utils.Point;

public class BuildingFactory
{

    public static Building createBuildingByTypeId(int type, Point location, Map map)
    {
        int buildingNum = 1;
        {
            Building b = map.getBuildings(type).getMax();
            if (b != null)
                buildingNum = b.getBuildingNum() + 1;
        }

        return createBuildingByType(BuildingValues.getBuildingClass(type), location, buildingNum);
    }

    public static <T extends Building> T createBuildingByType(Class<T> tClass, Point location, int buildingNum)
    {
        try
        {
            return tClass.getConstructor(Point.class, int.class).newInstance(location, buildingNum);
        }
        catch (Exception ex)
        {
            return null;
        }
    }
}