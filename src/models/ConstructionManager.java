package models;

import models.buildings.*;
import utils.Point;

import java.util.ArrayList;

import static models.buildings.ConstructMode.*;

public class ConstructionManager
{
    Village village;
    ArrayList<Construction> constructions;

    public ArrayList<Construction> getConstructions()
    {
        return constructions;
    }

    public void construct(int buildingType, Point location)
    {
        // TODO: 4/13/18 : define new method for getting a building with buildungType 
        Building building = null;
        // TODO: 4/13/18 : get constructTime from static information of BuildingInfo
        int constructTime = 0;
        ConstructMode constructMode;
        if (village.getMap().isValid(location))
            constructMode = CONSTRUCT;
        else
            constructMode = UPGRADE;

        Builder builder = village.getMap().getTownHall().getAvailableBuilder();
        int builderNum = builder.getBuilderNum();
        builder.setBuilderStatus(BuilderStatus.WORKING);
        Construction construction = new Construction(buildingType, village.getTurn(), constructTime, constructMode, location, builderNum, building);
        constructions.add(construction);


    }

    public void checkConstructions()
    {
        Construction construction;
        for (int i = 0; i < constructions.size(); i++)
        {
            construction = constructions.get(i);
            if (construction.isFinished(village.getTurn()))
            {
                construction.finishConstruction();
                Builder builder = village.getMap().getTownHall().getBuilderByNum(construction.getBuilderNum());
                builder.setBuilderStatus(BuilderStatus.FREE);
                constructions.remove(i);
                i--;
            }

        }
    }

}
