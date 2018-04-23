package models.soldiers;

import models.Resource;
import models.buildings.*;

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
}
