package models.buildings;

import models.*;
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

    public Construction(int buildingType, int constructTime, ConstructMode constructMode, Point location, Builder builder, Building building)
    {
        this.buildingType = buildingType;
        this.startTurn = World.sCurrentGame.getVillage().getTurn();
        this.constructTime = constructTime;
        this.constructMode = constructMode;
        this.location = location;
        this.builderNum = builder.getBuilderNum();
        builder.setBuilderStatus(BuilderStatus.WORKING);
        this.building = building;
    }

    public boolean isFinished()
    {
        return World.sCurrentGame.getVillage().getTurn() == startTurn + constructTime;
    }

    public void finishConstruction()
    {
        building.buildStatus = BuildStatus.BUILT;
        getBuilder().setBuilderStatus(BuilderStatus.FREE);
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
