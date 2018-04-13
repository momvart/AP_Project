package model.buildings;

import model.Resource;

public class DefensiveTowerInfo extends BuildingInfo
{
    DefenseType targetType;

    public DefensiveTowerInfo(int type, String name, Resource buildCost, int buildDuration, int destroyScore, Resource destroyResource, DefenseType targetType)
    {
        super(type, name, buildCost, buildDuration, destroyScore, destroyResource);
        this.targetType = targetType;
    }

    public DefenseType getTargetType()
    {
        return targetType;
    }
}
