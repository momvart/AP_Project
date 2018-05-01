package models.soldiers;

public class SoldierFactory
{
    public static Soldier createSoldierByTypeID(int type, int level)
    {
        try
        {
            return (Soldier)SoldierValues.sSoldierClasses[type - 1].getConstructor(int.class).newInstance(level);
        }
        catch (Exception ex)
        {
            return null;
        }
    }


    public static <T extends Soldier> T createSoldierByType(Class<T> tClass, int level)
    {
        try
        {
            return tClass.getConstructor(int.class).newInstance(level);
        }
        catch (Exception ex)
        {
            return null;
        }
    }

}
