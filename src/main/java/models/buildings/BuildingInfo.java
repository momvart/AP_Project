package models.buildings;

import com.google.gson.annotations.SerializedName;
import models.Resource;

public abstract class BuildingInfo
{
    private int type;
    private String name;
    private Resource buildCost;
    private int buildDuration;
    private int destroyScore;
    private Resource destroyResource;
    private int initialStrength;
    @SerializedName("ustginc")
    private int upgradeStrengthInc;

    public BuildingInfo()
    {

    }

    public BuildingInfo(int type, String name, Resource buildCost, int buildDuration, int destroyScore, Resource destroyResource, int initialStrength, int upgradeStrengthInc)
    {
        this.type = type;
        this.name = name;
        this.buildCost = buildCost;
        this.buildDuration = buildDuration;
        this.destroyScore = destroyScore;
        this.destroyResource = destroyResource;
        this.initialStrength = initialStrength;
        this.upgradeStrengthInc = upgradeStrengthInc;
    }

    public int getType()
    {
        return type;
    }

    public String getName()
    {
        return name;
    }

    public Resource getBuildCost()
    {
        return buildCost;
    }

    public Resource getUpgradeCost() {return getBuildCost();}

    public int getBuildDuration()
    {
        return buildDuration;
    }

    public int getDestroyScore()
    {
        return destroyScore;
    }

    public Resource getDestroyResource()
    {
        return destroyResource;
    }

    public int getInitialStrength()
    {
        return initialStrength;
    }

    public int getUpgradeStrengthInc()
    {
        return upgradeStrengthInc;
    }
}
