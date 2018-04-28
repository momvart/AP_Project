package models.soldiers;

public class Archer extends Soldier
{
    public Archer(int level)
    {
        super(level);
    }

    public static final int SOLDIER_TYPE = 4;

    public int getType()
    {
        return 4;
    }
}
