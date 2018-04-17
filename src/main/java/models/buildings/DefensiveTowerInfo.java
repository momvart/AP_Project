package models.buildings;

import models.Resource;

public class DefensiveTowerInfo extends BuildingInfo
{
    DefenseType targetType;

    public DefensiveTowerInfo(int type, String name, Resource buildCost, int buildDuration, int destroyScore, Resource destroyResource, DefenseType targetType,int initialStrength)
    {
        super(type, name, buildCost, buildDuration, destroyScore, destroyResource,initialStrength);
        this.targetType = targetType;
    }

    public DefenseType getTargetType()
    {
        return targetType;
    }
}
