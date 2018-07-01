package models.buildings;

import graphics.helpers.IOnConstructFinishListener;
import models.Builder;
import models.BuilderStatus;
import models.World;

public class Construction
{
    private int constructTime;
    private ConstructMode constructMode;
    private int builderNum;
    private int remainingTurn;
    private long buildingId;
    private transient IOnConstructFinishListener finishListener;
    private transient Building cachedBuilding;


    public Construction(Building building, int constructTime, ConstructMode constructMode, Builder builder)
    {
        this.constructTime = constructTime;
        this.constructMode = constructMode;
        this.builderNum = builder.getBuilderNum();
        builder.setBuilderStatus(BuilderStatus.WORKING);
        this.buildingId = building.getId();
        this.cachedBuilding = building;
        building.buildStatus = BuildStatus.IN_CONSTRUCTION;
        remainingTurn = constructTime;
    }

    public Building getBuilding()
    {
        if (cachedBuilding == null)
        {
            cachedBuilding = World.getVillage().getMap().getBuildingById(buildingId);
            setFinishListener(cachedBuilding.getVillageHelper().getGraphicHelper());
        }
        return cachedBuilding;
    }

    public void setFinishListener(IOnConstructFinishListener finishListener)
    {
        this.finishListener = finishListener;
    }

    public void callOnFinish()
    {
        if (finishListener != null)
            finishListener.onConstructFinish();
    }

    public boolean isFinished()
    {
        return remainingTurn <= 0;
    }

    public void finishConstruction()
    {
        getBuilding().setBuildStatus(BuildStatus.BUILT);
        if (constructMode.equals(ConstructMode.UPGRADE))
            getBuilding().upgrade();
        getBuilder().setBuilderStatus(BuilderStatus.FREE);
        callOnFinish();
    }

    public BuildingInfo getBuildingInfo() { return getBuilding().getBuildingInfo(); }

    public int getRemainingTurns()
    {
        return remainingTurn;
    }

    public int getBuilderNum()
    {
        return builderNum;
    }

    public Builder getBuilder()
    {
        return World.getVillage().getMap().getTownHall().getBuilderByNum(builderNum);
    }

    public void passTurn()
    {
        if (remainingTurn > 0)
            remainingTurn--;
    }

    @Override
    public String toString()
    {
        return getBuildingInfo().getName() + " " + getRemainingTurns() + " " + constructMode.toString();
    }
}
