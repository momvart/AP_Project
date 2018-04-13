package models;

import models.buildings.DefensiveTower;
import models.soldiers.Soldier;
import utils.Point;

import java.util.ArrayList;

public class Attack
{
    private ArrayList<Soldier> attackSoldiers;
    private ArrayList<Soldier> troopsToBeDeployed;
    private Resource claimedResorce;
    private Map map;
    private int turn;

    public void mapInfo()
    {

    }

    public void addUnit(Soldier soldier)
    {
        attackSoldiers.add(soldier);
        troopsToBeDeployed.add(soldier);
        soldier.participateIn(this);
    }

    public void putUnit(int unitType, int count, Point location)
    {
        ArrayList<Soldier> deployedTroops = new ArrayList<>();
        for (int i = 0; i < count && exists(unitType); i++)
        {
            for (Soldier soldier : troopsToBeDeployed)
            {
                if (soldier.getType() == unitType)
                {
                    soldier.setLocation(location);
                    deployedTroops.add(soldier);
                    break;
                }
            }
        }
        troopsToBeDeployed.removeAll(deployedTroops);

        if ()
        {

        }
    }

    private boolean exists(int unitType)
    {
        for (Soldier attackSoldier : troopsToBeDeployed)
        {
            if (attackSoldier.getType() == unitType)
            {
                return true;
            }
        }
        return false;
    }

    public void passTurn()
    {

    }

    public Resource getClaimedResorce()
    {
        return claimedResorce;
    }

    public ArrayList<Soldier> getUnits(int unitType)
    {
        ArrayList<Soldier> units = new ArrayList<>();
        for (Soldier attackSoldier : attackSoldiers)
        {
            if (attackSoldier.getType() == unitType)
            {
                units.add(attackSoldier);
            }
        }
        return units;
    }

    public ArrayList<Soldier> getAllUnits()
    {
        return attackSoldiers;
    }

    public ArrayList<DefensiveTower> getTowers(int towerType)
    {

    }

    public ArrayList<DefensiveTower> getAllTowers()
    {

    }

    public void quitAttack()
    {

    }

}
