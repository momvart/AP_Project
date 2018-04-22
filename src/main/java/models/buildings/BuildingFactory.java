package models.buildings;

import utils.Point;

public class BuildingFactory
{

    public static Building createBuildingByTypeId(int type, Point location)
    {
        switch (type)
        {
            case 1:
                return new GoldMine(location);
            case 2:
                return new ElixirMine(location);
            case 3:
                return new GoldStorage(location);
            case 4:
                return new ElixirStorage(location);
            case 5:
                return new TownHall(location);
            case 6:
                return new Barracks(location);
            case 7:
                return new Camp(location);
            case 8:
                return new ArcherTower(location);
            case 9:
                return new Cannon(location);
            case 10:
                return new AirDefense(location);
            case 11:
                return new WizardTower(location);
            case 12:
                //todo : wall implementation
                return null;
            case 13:
                return new Trap(location);
            case 14:
                return new GuardianGiant(location);
            default:
                throw new IllegalArgumentException("Building type is not valid: " + type);
        }
    }

    public static <T extends Building> T createBuildingByType(Class<T> tClass, Point location)
    {
        try
        {
            return tClass.getConstructor(Point.class).newInstance(location);
        }
        catch (Exception ex)
        {
            return null;
        }
    }
}