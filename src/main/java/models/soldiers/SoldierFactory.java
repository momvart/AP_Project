package models.soldiers;

public class SoldierFactory
{
    public static Soldier createSoldierByTypeID(int type , int level)
    {
        switch (type)
        {
            case 0:
                return new Guardian(level);
            case 1:
                return new Giant(level);
            case 2:
                return new Dragon(level);
            case 3:
                return new Archer(level);
            case 4:
                return new WallBreaker(level
                );
            case 5:
                return new Healer(level);
        }
        return null;
        // TODO: 4/22/18 : throw an exception here if needed.
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
