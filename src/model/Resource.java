package model;

public class Resource
{
    int gold;
    int elixir;

    public Resource(int gold, int elixir)
    {
        this.gold = gold;
        this.elixir = elixir;
    }

    public int getGold()
    {
        return gold;
    }

    public void setGold(int gold)
    {
        this.gold = gold;
    }

    public int getElixir()
    {
        return elixir;
    }

    public void setElixir(int elixir)
    {
        this.elixir = elixir;
    }
}
