package models.attack;

import models.Resource;

import java.util.List;
import java.util.UUID;

public class AttackReport
{
    private UUID attackerId;
    private UUID defenderId;
    private Resource totalResource;
    private Resource claimedResource;
    private int totalScore;
    private int claimedScore;

    private List<Integer> troopsCount;

    public AttackReport(UUID attackerId, UUID defenderId, Resource totalResource, Resource claimedResource, int totalScore, int claimedScore, List<Integer> troopsCount)
    {
        this.attackerId = attackerId;
        this.defenderId = defenderId;
        this.totalResource = totalResource;
        this.claimedResource = claimedResource;
        this.totalScore = totalScore;
        this.claimedScore = claimedScore;
        this.troopsCount = troopsCount;
    }

    public UUID getAttackerId()
    {
        return attackerId;
    }

    public void setAttackerId(UUID attackerId)
    {
        this.attackerId = attackerId;
    }

    public UUID getDefenderId()
    {
        return defenderId;
    }

    public void setDefenderId(UUID defenderId)
    {
        this.defenderId = defenderId;
    }

    public Resource getTotalResource()
    {
        return totalResource;
    }

    public Resource getClaimedResource()
    {
        return claimedResource;
    }

    public int getTotalScore()
    {
        return totalScore;
    }

    public int getClaimedScore()
    {
        return claimedScore;
    }

    public List<Integer> getTroopsCount()
    {
        return troopsCount;
    }
}
