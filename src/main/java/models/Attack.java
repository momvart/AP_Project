package models;

import exceptions.*;
import models.buildings.DefensiveTower;
import models.soldiers.Healer;
import models.soldiers.Soldier;
import models.soldiers.SoldierCollection;
import utils.Point;
import utils.Size;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Attack
{
    private final int MAX_SOLDIERS_IN‌_A_GREED = 5;
    private SoldierCollection soldiers = new SoldierCollection();
    private Resource claimedResource;
    private AttackMap map;
    private int turn;

    private SoldierCollection soldiersOnMap = new SoldierCollection();
    private SoldierCoordinatedCollection soldiersOnLocations;

    public PathFinder pathFinder = new PathFinder();

    public Attack(AttackMap map)
    {
        this.map = map;
        claimedResource = new Resource(0, 0);
        soldiersOnLocations = new SoldierCoordinatedCollection(map.getSize());
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

    public void putUnits(int unitType, int count, Point location) throws ConsoleException
    {
        if (!isValid(location))
            throw new ConsoleRuntimeException("Invalid location.", location + " is not a marginal location.", new IllegalArgumentException("Invalid location"));

        List<Soldier> available = getUnitsInToBeDeployed(unitType).collect(Collectors.toList());
        int current = numberOfSoldiersIn(location);
        if (MAX_SOLDIERS_IN‌_A_GREED - current < count)
        {
            throw new FilledCellException(location, "Current: " + current);
        }
        else if (available == null || available.size() < count)
        {
            throw new NotEnoughSoldierException(unitType, available == null ? 0 : available.size(), count);
        }
        else
        {
            for (int i = 0; i < count; i++)
            {
                putUnit(available.get(i), location);
            }
        }
    }

    private boolean isValid(Point location)
    {
        return location.getY() == 0 || location.getY() == 29 || location.getX() == 0 || location.getX() == 29;//TODO 0 , 29 could be changed later on.they are representing the edges of the 30x30 map‌
    }

    public int numberOfSoldiersIn(int x, int y)
    {
        return soldiersOnLocations.getSoldiers(x, y).size();
    }

    public int numberOfSoldiersIn(Point location)
    {
        return numberOfSoldiersIn(location.getX(), location.getY());
    }

    private void putUnit(Soldier soldier, Point location)
    {
        if (map.isValid(location))
        {
            soldier.setLocation(location);
            soldier.getAttackHelper().setSoldierIsDeployed(true);
            soldiersOnMap.addSoldier(soldier);
            soldiersOnLocations.push(soldier, location);
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

    private static class SoldierCoordinatedCollection
    {
        private ArrayList<LinkedList<Soldier>> soldiers;

        private Size size;

        public SoldierCoordinatedCollection(Size mapSize)
        {
            this.size = mapSize;
            soldiers = Stream.<LinkedList<Soldier>>generate(LinkedList::new).limit(size.getWidth() * size.getHeight()).collect(Collectors.toCollection(ArrayList::new));
        }

        public List<Soldier> getSoldiers(int x, int y)
        {
            return soldiers.get(y * size.getWidth() + x);
        }

        public List<Soldier> getSoldiers(Point location)
        {
            return getSoldiers(location.getX(), location.getY());
        }

        public void push(Soldier soldier, int x, int y)
        {
            getSoldiers(x, y).add(soldier);
        }

        public void push(Soldier soldier, Point location)
        {
            push(soldier, location.getX(), location.getY());
        }

        public boolean pull(Soldier soldier, int x, int y)
        {
            return getSoldiers(x, y).remove(soldier);
        }

        public boolean pull(Soldier soldier, Point location)
        {
            return pull(soldier, location.getX(), location.getY());
        }

        public void move(Soldier soldier, Point from, Point to)
        {
            pull(soldier, from);
            push(soldier, to);
        }
    }



}
