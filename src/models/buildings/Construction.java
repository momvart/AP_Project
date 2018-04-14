package models.buildings;

import models.Village;
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
    Village village;

    public Construction(int buildingType, int constructTime, ConstructMode constructMode, Point location, int builderNum, Building building, Village village)
    {
        this.buildingType = buildingType;
        this.startTurn = village.getTurn();
        this.constructTime = constructTime;
        this.constructMode = constructMode;
        this.location = location;
        this.builderNum = builderNum;
        this.building = building;
        this.village = village;
    }

    public boolean isFinished(int turn)
    {
        return turn == startTurn + constructTime;
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
        return startTurn + constructTime - village.getTurn();
    }

    public int getBuilderNum()
    {
        return builderNum;
    }

    @Override
    public String toString()
    {
        return getBuildingInfo().getName() + " " + getRemainingTurns();
    }
}
