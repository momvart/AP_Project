package models;

import java.util.ArrayList;

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

    @Override
    public String toString()
    {
        return toString(true);
    }

    public String toString(boolean showZero)
    {
        StringBuilder sb = new StringBuilder();
        if (gold > 0 || showZero)
            sb.append(String.format("%,d", gold)).append(" gold");
        if (elixir > 0 || showZero)
        {
            if (sb.length() > 0)
                sb.append(" + ");
            sb.append(String.format("%,d", elixir)).append(" elixir");
        }
        return sb.toString();
    }
}
