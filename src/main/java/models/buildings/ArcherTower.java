package models.buildings;

import models.Attack;
import utils.Point;

public class ArcherTower extends DefensiveTower
{
    public ArcherTower(Point location)
    {
        super(location);
    }

    @Override
    public void attack(Attack attack)
    {

    }

    @Override
    public int getType()
    {
        return 8;
    }
}
