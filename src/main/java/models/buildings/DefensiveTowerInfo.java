package models.buildings;

import com.google.gson.annotations.SerializedName;
import models.Resource;

public class DefensiveTowerInfo extends BuildingInfo
{
    private DefenseType targetType;

    @SerializedName("udmginc")
    private int upgradeDamageInc;


    public DefensiveTowerInfo(int type, String name, Resource buildCost, int buildDuration, int destroyScore, Resource destroyResource, int initialStrength, DefenseType targetType, int upgradeDamageInc, int upgradeStrengthInc)
    {
        super(type, name, buildCost, buildDuration, destroyScore, destroyResource, initialStrength, upgradeStrengthInc);
        this.targetType = targetType;
        this.upgradeDamageInc = upgradeDamageInc;
    }

    public DefenseType getTargetType()
    {
        return targetType;
    }

    public int getUpgradeDamageInc()
    {
        return upgradeDamageInc;
    }
}
