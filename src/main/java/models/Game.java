package models;

public class Game
{
    Village village = new Village();
    int score = 0;

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
