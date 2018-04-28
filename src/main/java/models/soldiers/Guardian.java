package models.soldiers;

public class Guardian extends Soldier
{

    public Guardian(int level)
    {
        super(level);
    }

    public static final int SOLDIER_TYPE = 1;

    public int getType()
    {
        return SOLDIER_TYPE;
    }
}
