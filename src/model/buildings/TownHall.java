package model.buildings;

public class TownHall extends VillageBuilding
{
    int goldCapactiy;
    int elixirCapacity;

    public int getGoldCapactiy()
    {
        return goldCapactiy;
    }

    public int getElixirCapacity()
    {
        return elixirCapacity;
    }

    @Override
    public void destroy()
    {

    }

    @Override
    public int getType()
    {
        return 0;
    }

    @Override
    public void upgrade()
    {
        super.upgrade();
    }

}
