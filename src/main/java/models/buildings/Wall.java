package models.buildings;

import utils.Point;

public class Wall extends Building
{
    public Wall(Point location, int buildingNum)
    {
        super(location, buildingNum);
    }

    @Override
    public int getType()
    {
        return 12;
    }
}
