package models.soldiers;

public class Dragon extends Soldier
{

    public Dragon(int level)
    {
        super(level);
    }

    public static final int SOLDIER_TYPE = 3;

    public int getType()
    {
        return SOLDIER_TYPE;
    }
}
