package models.soldiers;

import com.google.gson.*;
import com.google.gson.reflect.*;
import exceptions.ConsoleRuntimeException;

import java.nio.file.*;
import java.io.*;
import java.util.ArrayList;

public abstract class SoldierValues
{
    public static final int SOLDIER_TYPES_COUNT = 6;

    public static final Class[] sSoldierClasses = new Class[] { Guardian.class, Giant.class, Dragon.class, Archer.class, WallBreaker.class, Healer.class };


    private static ArrayList<SoldierInfo> infos;

    public static void initialize(Path path) throws IOException
    {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Class.class, new serialization.ClassAdapter())
                .create();
        try (BufferedReader reader = Files.newBufferedReader(path))
        {
            infos = new ArrayList<>();
            infos = gson.fromJson(reader, new TypeToken<ArrayList<SoldierInfo>>() { }.getType());
        }
    }

    public static SoldierInfo getSoldierInfo(int type)
    {
        return infos.get(type - 1);
    }

    public static ArrayList<SoldierInfo> getInfos() { return infos; }

    public static Class getSoldierClass(int soldierType)
    {
        try
        {
            return getSoldierInfo(soldierType).getSoldierClass();
        }
        catch (ArrayIndexOutOfBoundsException ex)
        {
            throw new IllegalArgumentException("Soldier type is not valid.", ex);
        }
    }

    public static int getTypeByName(String soldierType)
    {
        for (Class cls : sSoldierClasses)
            if (cls.getSimpleName().equalsIgnoreCase(soldierType))
                try
                {
                    return cls.getField("SOLDIER_TYPE").getInt(null);
                }
                catch (Exception ex)
                {

                }
        throw new ConsoleRuntimeException("Invalid soldier type", "Invalid soldier type: " + soldierType);
    }
}
