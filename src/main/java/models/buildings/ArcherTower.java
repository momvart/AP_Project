package models.buildings;

import models.Attack;
import utils.Point;

public class ArcherTower extends DefensiveTower
{
    public ArcherTower(Point location, int buildingNum)
    {
        super(location, buildingNum);
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
