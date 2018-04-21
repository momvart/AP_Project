package models;

import java.util.ArrayList;
import java.util.Comparator;

public class Resource implements Comparable<Resource>
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

    @Override
    public int compareTo(Resource o)
    {
        if (gold == o.gold && elixir == o.elixir)
            return 0;
        else
            return gold > o.gold ? 1 : -1;
    }

    public int compareTo(Resource o, boolean goldPrior)
    {
        if (gold == o.gold && elixir == o.elixir)
            return 0;
        else
            return (goldPrior ? gold > o.gold : elixir > o.elixir) ? 1 : -1;
    }

    public boolean isGreaterThanOrEqual(Resource o)
    {
        return gold >= o.gold && elixir >= o.elixir;
    }

    public boolean isLessThanOrEqual(Resource o) {return gold <= o.gold && elixir <= o.elixir;}

}
