package models;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javafx.stage.Stage;
import models.buildings.Building;
import models.buildings.BuildingValues;
import models.soldiers.Soldier;
import models.soldiers.SoldierValues;
import network.GameClientC;
import network.GameHost;
import serialization.BuildingAdapter;
import serialization.ClassAdapter;
import serialization.MapAdapter;
import serialization.SoldierAdapter;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class World
{
    public static Game sCurrentGame;
    public static Settings sSettings;
    public static Stage sMenuStage;

    public static void initialize()
    {
        try
        {
            BuildingValues.initialize(Paths.get(World.class.getClassLoader().getResource("BuildingValues.json").toURI()));
            SoldierValues.initialize(Paths.get(World.class.getClassLoader().getResource("SoldierValues.json").toURI()));
            loadSettings(Paths.get(World.class.getClassLoader().getResource("Settings.json").toURI()));
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }
        catch (Exception ex)
        {

        }
    }

    public static void loadSettings(Path path) throws IOException
    {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();
        try (BufferedReader reader = Files.newBufferedReader(path))
        {
            sSettings = gson.fromJson(reader, Settings.class);
        }
    }

    public static void saveSettings()
    {
        try
        {
            saveSettings(Paths.get(World.class.getClassLoader().getResource("Settings.json").toURI()));
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    public static void saveSettings(Path path) throws IOException
    {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();
        try (BufferedWriter writer = Files.newBufferedWriter(path))
        {
            gson.toJson(sSettings, Settings.class, writer);
        }
    }

    public static void passTurn()
    {
        sCurrentGame.passTurn();
    }

    public static void newGame()
    {
        loadGame(new Game()
        {{
            initialize();
            getVillage().getResources().increase(10000, 2000);
        }});
    }

    public static void loadGame(Game game)
    {
        sCurrentGame = game;
    }

    private static Gson createSerializer()
    {
        return new GsonBuilder()
                .registerTypeAdapter(Building.class, new BuildingAdapter())
                .registerTypeAdapter(Soldier.class, new SoldierAdapter())
                .registerTypeAdapter(Class.class, new ClassAdapter())
                .registerTypeAdapter(Map.class, new MapAdapter())
                .setPrettyPrinting()
                .create();
    }

    public static void openGame(Reader reader)
    {
        Gson g = createSerializer();
        loadGame(g.fromJson(reader, Game.class));
    }

    public static void saveGame(Writer writer)
    {
        Gson g = createSerializer();
        g.toJson(sCurrentGame, Game.class, writer);
    }

    public static Village getVillage()
    {
        return sCurrentGame.getVillage();
    }

    public static void showMenu()
    {
        sMenuStage.show();
    }


    public static GameClientC sCurrentClient;
    public static GameHost sCurrentHost;
}
