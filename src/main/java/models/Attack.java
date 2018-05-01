package models;

import models.buildings.DefenseType;
import models.buildings.DefensiveTower;
import models.soldiers.Healer;
import models.soldiers.Soldier;
import utils.PathFinder;
import models.soldiers.SoldierCollection;
import utils.Point;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Attack
{
    private SoldierCollection soldiers = new SoldierCollection();
    private Resource claimedResource;
    private AttackMap map;
    private int turn;
    public SoldierCollection soldiersOnMap = new SoldierCollection();
    private PathFinder pathFinder = new PathFinder();

    public Attack(AttackMap map)
    {
        this.map = map;
        claimedResource = new Resource(0, 0);
    }


    public void mapInfo()
    {

    }

    public void addUnit(Soldier soldier)
    {
        soldiers.addSoldier(soldier);
        soldier.participateIn(this);
    }

    public void addUnits(List<Soldier> soldierList)
    {
        soldierList.forEach(s ->
        {
            soldiers.addSoldier(s);
            s.participateIn(this);
        });
    }

    public void putUnits(int unitType, int count, Point location)
    {
        List<Soldier> available = getUnitsInToBeDeployed(unitType).collect(Collectors.toList());
        if (available.size() >= count)
            available.forEach(soldier -> putUnit(soldier, location));
    }

    private void putUnit(Soldier soldier, Point location)
    {
        if (map.isValid(location))
        {
            soldier.setLocation(location);
            soldier.getAttackHelper().setSoldierIsDeployed(true);
            soldiersOnMap.addSoldier(soldier);
        }
    }


    public Stream<Soldier> getUnitsInToBeDeployed(int unitType)
    {
        return getUnits(unitType).filter(s -> !s.getAttackHelper().isSoldierDeployed());
    }

    public void passTurn()
    {
        ageHealersOnMap();
        killAgedHealers();
        soldiersOnMap.getAllSoldiers()
                .filter(soldier -> !soldier.getAttackHelper().isDead())
                .forEach(soldier -> soldier.getAttackHelper().passTurn());

        turn++;
    }


    private void killAgedHealers()
    {
        getDeployedUnits(Healer.SOLDIER_TYPE).forEach(soldier ->
        {
            Healer healer = (Healer)soldier;
            if (healer.getTimeTillDie() <= 0)
            {
                healer.getAttackHelper().setDead(true);
            }
        });
    }

    private void ageHealersOnMap()
    {
        getDeployedUnits(Healer.SOLDIER_TYPE).forEach(soldier -> ((Healer)soldier).ageOneDeltaT());
    }

    public Resource getClaimedResource()
    {
        return claimedResource;
    }


    public Stream<Soldier> getUnits(int unitType)
    {
        return soldiers.getSoldiers(unitType).stream()
                .filter(soldier -> soldier != null && !soldier.getAttackHelper().isDead());
    }

    public Stream<Soldier> getDeployedUnits(int unitType)
    {
        return soldiersOnMap.getSoldiers(unitType).stream()
                .filter(soldier -> soldier != null && !soldier.getAttackHelper().isDead());
    }

    public Stream<Soldier> getAllDeployedUnits()
    {
        return soldiersOnMap.getAllSoldiers().filter(soldier -> soldier != null && !soldier.getAttackHelper().isDead());
    }

    public Stream<Soldier> getAllUnits()
    {
        return soldiers.getAllSoldiers();
    }

    public List<DefensiveTower> getAllTowers()
    {
        return map.getAllDefensiveTowers();
    }

    public List<DefensiveTower> getTowers(int towerType)
    {
        return map.getDefensiveTowers(towerType);
    }

    public void quitAttack()
    {

    }

    public AttackMap getMap()
    {
        return map;
    }

    public List<Soldier> getSoldiersInRange(Point location, int range)
    {
        return getAllDeployedUnits().filter(soldier -> getDistance(location, soldier.getLocation()) <= range).collect(Collectors.toList());
    }

    private double getDistance(Point source, Point destination)
    {
        return Math.sqrt((source.getX() - destination.getX()) * (source.getX() - destination.getX()) +
                (source.getY() - destination.getY()) * (source.getY() - destination.getY()));
    }
}
