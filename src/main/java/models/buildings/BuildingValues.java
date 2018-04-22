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

    private static ArrayList<BuildingInfo> infos;

    public static void initialize(Path path) throws IOException
    {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(BuildingInfo.class, new serialization.BuildingInfoAdapter())
                .create();
        BufferedReader reader = null;
        try
        {
            reader = Files.newBufferedReader(path);
            infos = new ArrayList<>();
            infos = gson.fromJson(reader, new TypeToken<ArrayList<BuildingInfo>>() { }.getType());
        }
        finally
        {
            if (reader != null)
                reader.close();
        }
    }

    public static BuildingInfo getBuildingInfo(int type)
    {
        return infos.get(type - 1);
    }

    public static ArrayList<BuildingInfo> getInfos() {return infos;}

    public static Class getBuildingClass(int type)
    {
        switch (type)
        {
            case 1:
                return GoldMine.class;
            case 2:
                return ElixirMine.class;
            case 3:
                return GoldStorage.class;
            case 4:
                return ElixirStorage.class;
            case 5:
                return TownHall.class;
            case 6:
                return Barracks.class;
            case 7:
                return Camp.class;
            case 8:
                return ArcherTower.class;
            case 9:
                return Cannon.class;
            case 10:
                return AirDefense.class;
            case 11:
                return WizardTower.class;
            case 12:
                //todo : wall implementation
                return null;
            case 13:
                return Trap.class;
            case 14:
                return GuardianGiant.class;
            default:
                throw new IllegalArgumentException("Building type is not valid: " + type);
        }
    }
}
