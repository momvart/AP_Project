package models.buildings;

import com.google.gson.annotations.SerializedName;
import models.Resource;

public class DefensiveTowerInfo extends BuildingInfo
{
    private DefenseType targetType;

    private int initialDamage;
    @SerializedName("udmginc")
    private int upgradeDamageInc;

    private int attackRange;

    public DefenseType getTargetType()
    {
        return targetType;
    }

    public int getInitialDamage()
    {
        return initialDamage;
    }

    public int getUpgradeDamageInc()
    {
        return upgradeDamageInc;
    }

    public int getAttackRange()
    {
        return attackRange;
    }
}
