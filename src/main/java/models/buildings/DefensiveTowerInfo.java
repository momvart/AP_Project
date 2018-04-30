package models.buildings;

import com.google.gson.annotations.SerializedName;
import models.Resource;

public class DefensiveTowerInfo extends BuildingInfo
{
    private DefenseType targetType;

    @SerializedName("udmginc")
    private int upgradeDamageInc;
    @SerializedName("ustginc")
    private int upgradeStrengthInc;

    public DefensiveTowerInfo(int type, String name, Resource buildCost, int buildDuration, int destroyScore, Resource destroyResource, int initialStrength, DefenseType targetType, int upgradeDamageInc, int upgradeStrengthInc)
    {
        super(type, name, buildCost, buildDuration, destroyScore, destroyResource, initialStrength);
        this.targetType = targetType;
        this.upgradeDamageInc = upgradeDamageInc;
        this.upgradeStrengthInc = upgradeStrengthInc;
    }

    public DefenseType getTargetType()
    {
        return targetType;
    }

    public int getUpgradeDamageInc()
    {
        return upgradeDamageInc;
    }

    public int getUpgradeStrengthInc()
    {
        return upgradeStrengthInc;
    }
}
