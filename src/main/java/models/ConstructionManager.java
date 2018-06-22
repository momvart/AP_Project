package models;

import exceptions.FilledCellException;
import exceptions.NoAvailableBuilderException;
import models.buildings.Building;
import models.buildings.BuildingFactory;
import models.buildings.Construction;
import utils.Point;

import java.util.ArrayList;

import static models.buildings.ConstructMode.CONSTRUCT;
import static models.buildings.ConstructMode.UPGRADE;

public class ConstructionManager
{
    private ArrayList<Construction> constructions = new ArrayList<>();

    public ArrayList<Construction> getConstructions()
    {
        return constructions;
    }

    private Village getVillage()
    {
        //TODO: change is necessary if it wants to manage multi villages
        return World.getVillage();
    }

    public void construct(int buildingType, Point location) throws NoAvailableBuilderException, FilledCellException
    {
        Building building = BuildingFactory.createBuildingByTypeId(buildingType, location, getVillage().getMap());
        int constructTime = building.getBuildingInfo().getBuildDuration();
        Builder builder = getVillage().getAvailableBuilder();
        World.getVillage().getMap().addBuilding(building);
        Construction construction = new Construction(building, constructTime, CONSTRUCT, builder);
        constructions.add(construction);
    }

    public void upgrade(Building building) throws NoAvailableBuilderException
    {
        int constructTime = building.getBuildingInfo().getBuildDuration();
        Builder builder = getVillage().getAvailableBuilder();
        Construction construction = new Construction(building, constructTime, UPGRADE, builder);
        constructions.add(construction);
    }

    public void checkConstructions()
    {
        Construction construction;
        for (int i = 0; i < constructions.size(); i++)
        {
            construction = constructions.get(i);
            construction.passTurn();
            if (construction.isFinished())
            {
                construction.finishConstruction();
                constructions.remove(i);
                i--;
            }
        }
    }
}
