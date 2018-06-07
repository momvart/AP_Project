package models.buildings;

import com.google.gson.reflect.TypeToken;
import models.Resource;
import com.google.gson.*;
import com.google.gson.stream.*;

import java.nio.file.*;
import java.io.*;
import java.util.ArrayList;

import static models.buildings.DefenseType.*;

public class BuildingValues
{
    public static final int BUILDING_TYPES_COUNT = 14;

    private static final Class[] sBuildingClasses = { GoldMine.class, ElixirMine.class, GoldStorage.class, ElixirStorage.class, TownHall.class, Barracks.class, Camp.class,
            ArcherTower.class, Cannon.class, AirDefense.class, WizardTower.class, Wall.class, Trap.class, GuardianGiant.class };

    private static ArrayList<BuildingInfo> infos;

    public static void initialize(Path path) throws IOException
    {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(BuildingInfo.class, new serialization.BuildingInfoAdapter())
                .create();
        try (BufferedReader reader = Files.newBufferedReader(path))
        {
            infos = new ArrayList<>();
            infos = gson.fromJson(reader, new TypeToken<ArrayList<BuildingInfo>>() { }.getType());
        }
    }

    public static BuildingInfo getBuildingInfo(int type)
    {
        return infos.get(type - 1);
    }

    public static ArrayList<BuildingInfo> getInfos() {return infos;}

    public static Class getBuildingClass(int type)
    {
        return sBuildingClasses[type - 1];
    }
}
