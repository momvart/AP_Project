package models.buildings;

import utils.Point;

public class GuardianGiant extends DefensiveTower
{
    public GuardianGiant(Point location, int buildingNum)
    {
        super(location, buildingNum);
    }



    @Override
    public int getType()
    {
        return 14;
    }

    //TODO: needs to override participateIn with a custom AttackHelper
}
