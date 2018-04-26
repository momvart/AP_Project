package models;

import models.buildings.DefensiveTower;
import models.soldiers.Healer;
import models.soldiers.Soldier;
import utils.Point;

import java.util.ArrayList;

public class Attack
{
    private ArrayList<Soldier> attackSoldiers;
    private Resource claimedResource;
    private AttackMap map;
    private int turn;
    public ArrayList<Soldier> soldiersOnMap = new ArrayList<>();
    private ArrayList<Healer> healersOnMap = new ArrayList<>();
    public void mapInfo()
    {

    }

    public void addUnit(Soldier soldier)
    {
        attackSoldiers.add(soldier);
        soldier.participateIn(this);
    }

    public void putUnits(int unitType, int count, Point location)
    {
        if(getUnitsInToBeDeplyed(unitType) != null && getUnitsInToBeDeplyed(unitType).size() >= count)//TODO‌ Exceptions of more than 5 soldiers should be handled later on , when we come to an agreemaent of how to suppose the soldiers on the map
        {
            for (int i = 0; i < count ; i++)
            {
                putUnit(getUnitsInToBeDeplyed(unitType).get(i)  ,  location);
            }
        }
    }

    private void putUnit(Soldier soldier, Point location)
    {
        if (map.isValid(location))
        {
            soldier.setLocation(location);
            soldier.getAttackHelper().setSoldierIsDeployed(true);
            soldiersOnMap.add(soldier);
            if (soldier.getType() == 5)//TODO‌ Here 5 is type of the healer.TO be checked if there is a change in initial SoldierValues
            {
                healersOnMap.add((Healer)soldier);
            }
        }
    }

    public ArrayList<Soldier> getSoldiersToBeDeployed()
    {
        ArrayList<Soldier> troopsToBeDeplyed = new ArrayList<>();
        for (Soldier attackSoldier : attackSoldiers)
        {
            if (!attackSoldier.getAttackHelper().isDead() && !attackSoldier.getAttackHelper().isSoldierDeployed())
            {
                troopsToBeDeplyed.add(attackSoldier);
            }
        }
        return troopsToBeDeplyed;
    }

    public ArrayList<Soldier> getUnitsInToBeDeplyed(int unitType)
    {
        ArrayList<Soldier> reqTroops = new ArrayList<>();
        for (Soldier soldier : getSoldiersToBeDeployed())
        {
            if (soldier != null && !soldier.getAttackHelper().isDead() && soldier.getType() == unitType)
            {
                reqTroops.add(soldier);
            }
        }
        return reqTroops;
    }

    public void passTurn()
    {
        ageHealersOnMap();
        killAgedHealers();
        for (Soldier soldier : soldiersOnMap)
        {
            if (soldier != null && !soldier.getAttackHelper().isDead())
            {
                soldier.getAttackHelper().passTurn();
            }
        }
        turn++;
    }


    private void killAgedHealers()
    {
        for (Healer healer : healersOnMap)
        {
            if (healer != null && !healer.getAttackHelper().isDead())
            {
                if (healer.getTimeTillDie() <= 0)
                {
                    healer.getAttackHelper().setDead(true);
                    healer = null;
                }
            }
        }
    }

    private void ageHealersOnMap()
    {
        for (Healer healer : healersOnMap)
        {
            if (healer != null && !healer.getAttackHelper().isDead())
            {
                healer.ageOneDeltaT();
            }
        }
    }

    public Resource getClaimedResource()
    {
        return claimedResource;
    }


    public ArrayList<Soldier> getUnits(int unitType)
    {
        ArrayList<Soldier> units = new ArrayList<>();
        for (Soldier attackSoldier : attackSoldiers)
        {
            if (attackSoldier != null && !attackSoldier.getAttackHelper().isDead() && attackSoldier.getType() == unitType)
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
        ArrayList<DefensiveTower> requiredTowers = new ArrayList<>();
        for (DefensiveTower defensiveTower : map.getDefensiveTowers())
        {
            if (defensiveTower != null && defensiveTower.getType() == towerType)
            {
                requiredTowers.add(defensiveTower);
            }
        }
        return requiredTowers;
    }

    public ArrayList<DefensiveTower> getAllTowers()
    {
        return map.getDefensiveTowers();
    }

    public void quitAttack()
    {

    }

    public Map getMap()
    {
        return map;
    }
}
