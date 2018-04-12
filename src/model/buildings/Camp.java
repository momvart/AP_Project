package model.buildings;

import java.util.ArrayList;

public class Camp extends VillageBuilding
{
    int capacity;
    ArrayList<Soldier> soldiers;

    public int getCapacity()
    {
        return capacity;
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

    public void heal()
    {

    }
}
