package models.buildings;

public class ElixirMine extends Mine
{
    public ElixirMine()
    {
        setResourceAddPerDeltaT(5);
    }

    @Override
    public void mine(Storage storage)
    {

    }

    @Override
    public int getType()
    {
        return 2;
    }
}
