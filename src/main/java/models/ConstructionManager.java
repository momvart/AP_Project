package models;

import exceptions.NoAvailableBuilderException;
import models.buildings.*;
import utils.Point;

import java.util.ArrayList;

import static models.buildings.ConstructMode.*;

public class ConstructionManager
{
    public ConstructionManager(Village village)
    {
        this.village = village;
    }

    private transient Village village;
    private ArrayList<Construction> constructions = new ArrayList<>();

    public ArrayList<Construction> getConstructions()
    {
        return constructions;
    }

    public void construct(int buildingType, Point location) throws NoAvailableBuilderException
    {
        Building building = BuildingFactory.createBuildingByTypeId(buildingType, location);
        int constructTime = building.getBuildingInfo().getBuildDuration();
        Builder builder = village.getAvailableBuilder();
        World.getVillage().getMap().addBuilding(building);
        Construction construction = new Construction(buildingType, constructTime, CONSTRUCT, location, builder, building);
        constructions.add(construction);
    }

    public void upgrade(Building building) throws NoAvailableBuilderException
    {
        int constructTime = building.getBuildingInfo().getBuildDuration();
        Builder builder = village.getAvailableBuilder();
        Construction construction = new Construction(building.getType(), constructTime, UPGRADE, building.getLocation(), builder, building);
        constructions.add(construction);
    }

    public void checkConstructions()
    {
        Construction construction;
        for (int i = 0; i < constructions.size(); i++)
        {
            construction = constructions.get(i);
            if (construction.isFinished())
            {
                construction.finishConstruction();
                constructions.remove(i);
                i--;
            }
        }
    }
}
