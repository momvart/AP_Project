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

        switch (type)
        {
            case 1:
                return new GoldMine(location, buildingNum);
            case 2:
                return new ElixirMine(location, buildingNum);
            case 3:
                return new GoldStorage(location, buildingNum);
            case 4:
                return new ElixirStorage(location, buildingNum);
            case 5:
                return new TownHall(location, buildingNum);
            case 6:
                return new Barracks(location, buildingNum);
            case 7:
                return new Camp(location, buildingNum);
            case 8:
                return new ArcherTower(location, buildingNum);
            case 9:
                return new Cannon(location, buildingNum);
            case 10:
                return new AirDefense(location, buildingNum);
            case 11:
                return new WizardTower(location, buildingNum);
            case 12:
                //todo : wall implementation
                return null;
            case 13:
                return new Trap(location, buildingNum);
            case 14:
                return new GuardianGiant(location, buildingNum);
            default:
                throw new IllegalArgumentException("Building type is not valid: " + type);
        }
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