package models.buildings;

import utils.Point;

public abstract class VillageBuilding extends Building
{
    public VillageBuilding(Point location, int buildingNum)
    {
        super(location, buildingNum);
    }
}
