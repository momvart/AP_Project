package model.buildings;

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

    public Construction(int buildingType, int startTurn, int constructTime, ConstructMode constructMode, Point location, int builderNum, Building building)
    {
        this.buildingType = buildingType;
        this.startTurn = startTurn;
        this.constructTime = constructTime;
        this.constructMode = constructMode;
        this.location = location;
        this.builderNum = builderNum;
        this.building = building;
    }

    public boolean isFinished()
    {
        return false;
    }

    public void finishConstruction()
    {

    }

    public int getBuildingType()
    {
        return buildingType;
    }

    public int getRemainingTurns()
    {
        return 0;
    }

    public int getBuilderNum()
    {
        return builderNum;
    }

}
