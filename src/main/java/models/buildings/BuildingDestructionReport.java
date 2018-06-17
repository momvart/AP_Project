package models.buildings;

public class BuildingDestructionReport
{
    Building building;
    int initialStrength;
    int finalStrength;

    public BuildingDestructionReport(Building building, int initialStrength, int finalStrength)
    {
        this.building = building;
        this.initialStrength = initialStrength;
        this.finalStrength = finalStrength;
    }

    public Building getBuilding()
    {
        return building;
    }

    public int getInitialStrength()
    {
        return initialStrength;
    }

    public int getFinalStrength()
    {
        return finalStrength;
    }
}
