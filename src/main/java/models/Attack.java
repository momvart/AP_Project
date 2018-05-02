package models;

import exceptions.ConsoleException;
import exceptions.FilledCellException;
import exceptions.NotEnoughSoldierException;
import exceptions.SoldierNotFoundException;
import models.buildings.DefensiveTower;
import models.soldiers.Healer;
import models.soldiers.Soldier;
import models.soldiers.SoldierCollection;
import utils.PathFinder;
import utils.Point;

import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Attack
{
    private final int MAX_SOLDIERS_IN‌_A_GREED = 5;
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

    public void putUnits(int unitType, int numberOfSoldiers, Point location) throws ConsoleException
    {
        if (isValid(location))
        {
            List<Soldier> available = getUnitsInToBeDeployed(unitType).collect(Collectors.toList());
            if (MAX_SOLDIERS_IN‌_A_GREED - numberOfSoldiersIn(location) < numberOfSoldiers)
            {
                throw new FilledCellException(location);
            }
            else if (available == null || available.size() < numberOfSoldiers)
            {
                throw new NotEnoughSoldierException(unitType, available == null ? 0 : available.size(), numberOfSoldiers);
            }
            else
            {
                for (int i = 0; i < numberOfSoldiers; i++)
                {
                    putUnit(available.get(i), location);
                }
            }
        }
        //else throw new InvalidInitialLocationException
    }

    private boolean isValid(Point location)
    {
        return location.getY() == 0 || location.getY() == 29 || location.getX() == 0 || location.getX() == 29;//TODO 0 , 29 could be changed later on.they are representing the edges of the 30x30 map‌
    }

    private int numberOfSoldiersIn(Point location)
    {
        return 0;//TODO‌ to be implemented later on.
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

    public Soldier getNearestSoldier(Point location, int range) throws SoldierNotFoundException
    {
        try
        {
            Soldier s = getAllDeployedUnits().min(Comparator.comparing(soldier -> getDistance(soldier.getLocation(), location))).get();
            if (getDistance(location, s.getLocation()) <= range)
                return s;
            else
                throw new SoldierNotFoundException("No soldier in this range", "SoldierNotFound");
        }
        catch (NoSuchElementException ex)
        {
            throw new SoldierNotFoundException("No soldier in this range", "SoldierNotFound");
        }
    }

    private double getDistance(Point source, Point destination)
    {
        return Math.sqrt((source.getX() - destination.getX()) * (source.getX() - destination.getX()) +
                (source.getY() - destination.getY()) * (source.getY() - destination.getY()));
    }
}
