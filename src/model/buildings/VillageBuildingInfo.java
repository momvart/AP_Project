package model.buildings;

import model.Resource;

public class VillageBuildingInfo extends BuildingInfo
{
    public VillageBuildingInfo(int type, String name, Resource buildCost, int buildDuration, int destroyScore, Resource destroyResource)
    {
        super(type, name, buildCost, buildDuration, destroyScore, destroyResource);
    }
}
