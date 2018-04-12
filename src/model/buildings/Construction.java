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
