package models.buildings;

import models.Attack;

public class AirDefense extends DefensiveTower
{

    @Override
    public void attack(Attack attack)
    {

    }

    @Override
    public int getType()
    {
        return 10;
    }
}
