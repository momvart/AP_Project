package models;


import models.buildings.BuildingValues;
import models.soldiers.SoldierValues;

import java.io.IOException;
import java.nio.file.Paths;

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

    }

    public static void newGame()
    {
        //TODO: check value with document
        loadGame(new Game()
        {{
            getVillage().getResources().increase(2000, 2000);
        }});
    }

    public static void loadGame(Game game)
    {
        //TODO: add additional settings
        sCurrentGame = game;
    }

    public static void saveGame(String path, String name)
    {

    }

    public static Village getVillage()
    {
        return sCurrentGame.getVillage();
    }
}
