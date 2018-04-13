package models;

public class Game
{
    Village village;
    int score;

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

    }
}
