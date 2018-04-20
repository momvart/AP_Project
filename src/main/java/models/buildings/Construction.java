package models.buildings;

import models.Village;
import models.World;
import utils.Point;

public class Construction
{
    int buildingType;
    int startTurn;
    int constructTime;
    ConstructMode constructMode;
    Point location;
    int builderNum;
    Building building;
    boolean finished = false;

    public Construction(int buildingType, int constructTime, ConstructMode constructMode, Point location, int builderNum, Building building)
    {
        this.buildingType = buildingType;
        this.startTurn = World.sCurrentGame.getVillage().getTurn();
        this.constructTime = constructTime;
        this.constructMode = constructMode;
        this.location = location;
        this.builderNum = builderNum;
        this.building = building;
    }

    public boolean isFinished()
    {
        return World.sCurrentGame.getVillage().getTurn() == startTurn + constructTime;
    }

    public void finishConstruction()
    {
        building.buildStatus = BuildStatus.BUILT;
        finished = true;

    }

    public int getBuildingType()
    {
        return buildingType;
    }

    public BuildingInfo getBuildingInfo() {return BuildingValues.getBuildingInfo(buildingType);}

    public int getRemainingTurns()
    {
        return startTurn + constructTime - World.sCurrentGame.getVillage().getTurn();
    }

    public int getBuilderNum()
    {
        return builderNum;
    }

    @Override
    public String toString()
    {
        return getBuildingInfo().getName() + " " + getRemainingTurns() + " " + constructMode.toString();
    }
}
