package models;

import java.util.ArrayList;
import java.util.Comparator;

public class Resource implements Comparable<Resource>
{
    private int gold;
    private int elixir;

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

    public Resource decrease(Resource resource)
    {
        if (resource.gold > gold || resource.elixir > elixir)
            throw new IllegalArgumentException("not enough resource to decrease");
        this.gold -= resource.gold;
        this.elixir -= resource.elixir;
        return this;
    }

    public Resource increase(Resource resource)
    {
        gold += resource.gold;
        elixir += resource.elixir;
        return this;
    }

    public Resource increase(int gold, int elixir)
    {
        this.gold += gold;
        this.elixir += elixir;
        return this;
    }

    public static Resource multiply(Resource resource, int multiplier)
    {
        if (multiplier < 0)
            throw new IllegalArgumentException("Multiplier is negative: " + multiplier);
        return new Resource(resource.gold * multiplier, resource.elixir * multiplier);
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

    public boolean isGreaterThanOrEqual(Resource r)
    {
        return gold >= r.gold && elixir >= r.elixir;
    }

    public boolean isLessThanOrEqual(Resource r) {return gold <= r.gold && elixir <= r.elixir;}

    public boolean isLessThan(Resource r)
    {
        return !isGreaterThanOrEqual(r);
    }

    public static Resource subtract(Resource left, Resource right)
    {
        return new Resource(left.gold - right.gold, left.elixir - right.elixir);
    }
}
