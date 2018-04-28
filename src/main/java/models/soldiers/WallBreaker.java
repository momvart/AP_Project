package models.soldiers;

public class WallBreaker extends Soldier
{

    public WallBreaker(int level)
    {
        super(level);
    }

    public static final int SOLDIER_TYPE = 5;

    public int getType()
    {
        return SOLDIER_TYPE;
    }
}
