package models.buildings;

import models.Resource;

import java.util.ArrayList;

import static models.buildings.DefenseType.*;

public class BuildingValues
{
    public static final int BUILDING_TYPES_COUNT = 14;

    private static ArrayList<BuildingInfo> infos;

    public static void initialize()
    {
        //TODO: read from file or set info's
        String[] names = { "Gold mine", "Elixir mine", "Gold storage", "Elixir storage", "Main building", "Barracks",
                "Camp", "Archer tower", "Cannon", "Air defense", "Wizard tower", "Wall", "Trap", "Guardian Giant" };
        Resource[] buildCosts = { new Resource(150, 5), new Resource(100, 3), new Resource(200, 0)
                , new Resource(200, 0), new Resource(200, 0), new Resource(200, 0), new Resource(200, 0)
                , new Resource(300, 0), new Resource(400, 0), new Resource(300, 0), new Resource(500, 0)
                , new Resource(100, 0), new Resource(100, 0), new Resource(10000, 0) };
        int[] buildDurations = { 100, 100, 100, 100, 100, 100, 100, 60, 100, 60, 120, 20, 40, 4000 };
        int[] destroyScores = { 2, 2, 3, 3, 8, 1, 1, 3, 4, 3, 5, 1, 1, 6 };
        DefenseType[] defenseTypes = { GROUND, GROUND, AIR, BOTH, GROUND, GROUND };

        for (int i = 0; i < 7; i++)
        {
            VillageBuildingInfo villageBuildingInfo = new VillageBuildingInfo(i, names[i], buildCosts[i], buildDurations[i], destroyScores[i], buildCosts[i]);
            infos.add(villageBuildingInfo);
        }
        for (int i = 7; i < 14; i++)
        {
            if (i == 11)
            {
                VillageBuildingInfo villageBuildingInfo = new VillageBuildingInfo(i, names[i], buildCosts[i], buildDurations[i], destroyScores[i], buildCosts[i]);
                infos.add(villageBuildingInfo);
                continue;
            }
            DefensiveTowerInfo defensiveTowerInfo = new DefensiveTowerInfo(i, names[i], buildCosts[i], buildDurations[i], destroyScores[i], buildCosts[i], defenseTypes[i - 7]);
            infos.add(defensiveTowerInfo);
        }
    }

    public static BuildingInfo getBuildingInfo(int type)
    {
        return infos.get(type - 1);
    }
}
