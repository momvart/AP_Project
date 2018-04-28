package models.soldiers;

public class Giant extends Soldier
{

    public Giant(int level)
    {
        super(level);
    }

    public static final int SOLDIER_TYPE = 2;

    public int getType()
    {
        return SOLDIER_TYPE;
    }
}
