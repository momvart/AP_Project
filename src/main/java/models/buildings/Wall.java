package models.buildings;

import utils.Point;

public class Wall extends Building
{
    public Wall(Point location)
    {
        super(location);
    }

    @Override
    public int getType()
    {
        return 12;
    }
}
