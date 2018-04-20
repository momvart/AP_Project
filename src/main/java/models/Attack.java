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
    private Map map;
    private int turn;

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
        }
    }

    public ArrayList<Soldier> getSoldiersToBeDeployed()
    {
        ArrayList<Soldier> troopsToBeDeplyed = new ArrayList<>();
        for (Soldier attackSoldier : attackSoldiers)
        {
            if (attackSoldier.getAttackHelper().isSoldierDeployed() == false)
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
            if (soldier.getType() == unitType)
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
        removeDeadUnits();
        moveSoldiers();
        setNewTargetToSoldiers();
        fireSoldiers();
        turn++;
    }

    private void removeDeadUnits()
    {
        ArrayList<Soldier> walkingDeadSoldiers = new ArrayList<>();
        for (Soldier attackSoldier : attackSoldiers)
        {
            if (attackSoldier.getHealth() <= 0)
            {
                walkingDeadSoldiers.add(attackSoldier);
            }
        }
        attackSoldiers.removeAll(walkingDeadSoldiers);
    }

    private void killAgedHealers()
    {
        ArrayList<Healer> walkingDeadHealers = new ArrayList<>();
        for (Healer healer : getHealersOnMap())
        {
            if (healer.getTimeTillDie() <= 0)
            {
                walkingDeadHealers.add(healer);
            }
        }
        attackSoldiers.removeAll(walkingDeadHealers);
    }

    private void ageHealersOnMap()
    {

        for (Healer healer : getHealersOnMap())
        {
            healer.ageOneDeltaT();
        }
    }

    private ArrayList<Healer> getHealersOnMap()
    {
        ArrayList<Healer> healers = new ArrayList<>();
        for (Soldier soldier : getSoldiersOnMap())
        {
            if (soldier.getType() == new Healer().getType())
            {
                healers.add((Healer)soldier);
            }
        }
        return healers;
    }


    private void setNewTargetToSoldiers()
    {
        for (Soldier soldier : getSoldiersOnMap())
        {
            soldier.getAttackHelper().setTarget();
        }
    }


    private void fireSoldiers()
    {
        for (Soldier soldier : getSoldiersOnMap())
        {
            soldier.getAttackHelper().fire();
        }
    }

    private void moveSoldiers()
    {
        for (Soldier soldier : getSoldiersOnMap())
        {
            soldier.getAttackHelper().move();
        }
    }

    public Resource getClaimedResource()
    {
        return claimedResource;
    }

    public ArrayList<Soldier> getSoldiersOnMap()
    {
        ArrayList<Soldier> deployedSoldiers = new ArrayList<>();
        for (Soldier attackSoldier : attackSoldiers)
        {
            if (attackSoldier.getAttackHelper().isSoldierDeployed())
            {
                deployedSoldiers.add(attackSoldier);
            }
        }
        return deployedSoldiers;
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
        ArrayList<DefensiveTower> requiredTowers = new ArrayList<>();
        for (DefensiveTower defensiveTower : map.getDefensiveTowers())
        {
            if (defensiveTower.getType() == towerType)
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
