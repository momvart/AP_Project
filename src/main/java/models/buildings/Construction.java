package models.buildings;

import models.*;
import models.Village;
import models.World;
import utils.Point;

public class Construction
{
    private int startTurn;
    private int constructTime;
    private ConstructMode constructMode;
    private int builderNum;
    private long buildingId;

    public Construction(Building building, int constructTime, ConstructMode constructMode, Builder builder)
    {
        this.startTurn = World.sCurrentGame.getVillage().getTurn();
        this.constructTime = constructTime;
        this.constructMode = constructMode;
        this.builderNum = builder.getBuilderNum();
        builder.setBuilderStatus(BuilderStatus.WORKING);
        this.buildingId = building.getId();
        this.cachedBuilding = building;
        building.buildStatus = BuildStatus.IN_CONSTRUCTION;
    }

    private transient Building cachedBuilding;

    public Building getBuilding()
    {
        if (cachedBuilding == null)
            cachedBuilding = World.getVillage().getMap().getBuildingById(buildingId);
        return cachedBuilding;
    }

    public boolean isFinished()
    {
        return World.getVillage().getTurn() == startTurn + constructTime;
    }

    public void finishConstruction()
    {
        // TODO: 5/2/18 Check this part
        getBuilding().setBuildStatus(BuildStatus.BUILT);
        if (constructMode.equals(ConstructMode.UPGRADE))
            getBuilding().upgrade();
        getBuilder().setBuilderStatus(BuilderStatus.FREE);
    }

    public BuildingInfo getBuildingInfo() { return getBuilding().getBuildingInfo(); }

    public int getRemainingTurns()
    {
        return startTurn + constructTime - World.getVillage().getTurn();
    }

    public int getBuilderNum()
    {
        return builderNum;
    }

    public Builder getBuilder()
    {
        return World.getVillage().getMap().getTownHall().getBuilderByNum(builderNum);
    }

    @Override
    public String toString()
    {
        return getBuildingInfo().getName() + " " + getRemainingTurns() + " " + constructMode.toString();
    }
}
