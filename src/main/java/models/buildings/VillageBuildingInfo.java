package models.buildings;

import models.Resource;

public class VillageBuildingInfo extends BuildingInfo
{
    public VillageBuildingInfo(int type, String name, Resource buildCost, int buildDuration, int destroyScore, Resource destroyResource, int initialStrength, int upgradeStrengthInc)
    {
        super(type, name, buildCost, buildDuration, destroyScore, destroyResource, initialStrength, upgradeStrengthInc);
    }
}
