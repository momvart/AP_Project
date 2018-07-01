package models;

import java.util.ArrayList;

public class Settings
{
    private ArrayList<String> attackMapPaths;
    private double gameSpeed = 1;

    public Settings()
    {
    }


    public ArrayList<String> getAttackMapPaths()
    {
        return attackMapPaths;
    }

    public double getGameSpeed()
    {
        return gameSpeed;
    }

    public void setGameSpeed(double gameSpeed)
    {
        this.gameSpeed = gameSpeed;
    }
}
