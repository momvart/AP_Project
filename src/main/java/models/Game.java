package models;

public class Game
{
    private Village village;
    private int score = 0;

    public Game()
    {

    }

    public void initialize()
    {
        village = new Village();
        village.initialize();
    }

    public Village getVillage()
    {
        return village;
    }

    public void setVillage(Village village)
    {
        this.village = village;
    }

    public int getScore()
    {
        return score;
    }

    public void addScore(int score)
    {
        this.score += score;
    }

    public void passTurn()
    {
        village.passTurn();
    }
}
