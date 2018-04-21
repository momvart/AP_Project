package models;


import models.buildings.BuildingValues;

public class World
{
    public static Game sCurrentGame;

    public static void initialize()
    {
        BuildingValues.initialize();
    }

    public static void passTurn()
    {

    }

    public static void newGame()
    {
        loadGame(new Game());
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
