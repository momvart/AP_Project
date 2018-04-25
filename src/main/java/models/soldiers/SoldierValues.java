package models.soldiers;

import com.google.gson.*;
import com.google.gson.reflect.*;

import java.nio.file.*;
import java.io.*;
import java.util.ArrayList;

public abstract class SoldierValues
{
    public static final int SOLDIER_TYPES_COUNT = 6;

    private static ArrayList<SoldierInfo> infos;

    public static void initialize(Path path) throws IOException
    {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Class.class, new serialization.ClassAdapter())
                .create();
        BufferedReader reader = null;
        try
        {
            reader = Files.newBufferedReader(path);
            infos = new ArrayList<>();
            infos = gson.fromJson(reader, new TypeToken<ArrayList<SoldierInfo>>() { }.getType());
        }
        finally
        {
            if (reader != null)
                reader.close();
        }
    }

    public static SoldierInfo getSoldierInfo(int type)
    {
        return infos.get(type - 1);
    }

    public static ArrayList<SoldierInfo> getInfos() { return infos; }

    public static Class getSoldierClass(int soldierType)
    {
        switch (soldierType)
        {
            case 1:
                return Guardian.class;
            case 2:
                return Giant.class;
            case 3:
                return Dragon.class;
            case 4:
                return Archer.class;
            case 5:
                return WallBreaker.class;
            case 6:
                return Healer.class;
            default:
                throw new IllegalArgumentException("Soldier type is not valid.");
        }
    }
}
