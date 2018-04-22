package models.soldiers;

public class Archer extends Soldier
{
    public Archer(int level)
    {
        super(level);
    }

    public int getType()
    {
        return 3;
    }
}
