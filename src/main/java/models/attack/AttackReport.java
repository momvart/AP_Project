package models.attack;

import models.Resource;

import java.util.UUID;

public class AttackReport
{
    private UUID attackerId;
    private UUID defenderId;
    private Resource totalResource;
    private Resource gainedResource;
    private int totalTrophies;
    private int gainedTrophies;

    public AttackReport(UUID attackerId, UUID defenderId, Resource totalResource, Resource gainedResource, int totalTrophies, int gainedTrophies)
    {
        this.attackerId = attackerId;
        this.defenderId = defenderId;
        this.totalResource = totalResource;
        this.gainedResource = gainedResource;
        this.totalTrophies = totalTrophies;
        this.gainedTrophies = gainedTrophies;
    }

    public UUID getAttackerId()
    {
        return attackerId;
    }

    public UUID getDefenderId()
    {
        return defenderId;
    }

    public Resource getTotalResource()
    {
        return totalResource;
    }

    public Resource getGainedResource()
    {
        return gainedResource;
    }

    public int getTotalTrophies()
    {
        return totalTrophies;
    }

    public int getGainedTrophies()
    {
        return gainedTrophies;
    }
}
