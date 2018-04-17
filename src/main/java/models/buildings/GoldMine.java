package models.buildings;

public class GoldMine extends Mine
{
    public GoldMine()
    {
        setResourceAddPerDeltaT(10);
    }

    @Override
    public void mine(Storage storage)
    {

    }

    @Override
    public int getType()
    {
        return 1;
    }
}
