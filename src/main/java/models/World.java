package models;


import models.buildings.*;
import models.buildings.BuildingValues;
import models.soldiers.SoldierValues;

import java.io.*;
import java.nio.file.*;

import com.google.gson.*;
import com.google.gson.stream.*;
import com.google.gson.reflect.*;
import serialization.*;

public class World
{
    public static Game sCurrentGame;

    public static void initialize()
    {
        try
        {
            BuildingValues.initialize(Paths.get(World.class.getClassLoader().getResource("BuildingValues.json").toURI()));
            SoldierValues.initialize(Paths.get(World.class.getClassLoader().getResource("SoldierValues.json").toURI()));
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }
        catch (Exception ex)
        {

        }
    }

    public static void passTurn()
    {
        sCurrentGame.passTurn();
    }

    public static void newGame()
    {
        //TODO: check value with document
        loadGame(new Game()
        {{
            initialize();
            getVillage().getResources().increase(2000, 2000);
        }});
    }

    public static void loadGame(Game game)
    {
        //TODO: add additional settings
        sCurrentGame = game;
    }

    private static Gson createSerializer()
    {
        return new GsonBuilder()
                .registerTypeAdapter(Building.class, new BuilderAdapter())
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
}
