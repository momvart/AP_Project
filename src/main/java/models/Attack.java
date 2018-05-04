package models;

import exceptions.*;
import models.buildings.DefensiveTower;
import models.soldiers.Healer;
import models.soldiers.Soldier;
import models.soldiers.SoldierCollection;
import utils.MapCellNode;
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
    private int claimedScore;
    private AttackMap map;
    private int turn;

    private SoldierCollection soldiersOnMap = new SoldierCollection();
    private SoldierCoordinatedCollection soldiersOnLocations;

    private PathFinder pathFinder = new PathFinder();

    public Attack(AttackMap map)
    {
        this.map = map;
        claimedResource = new Resource(0, 0);
        soldiersOnLocations = new SoldierCoordinatedCollection(map.getSize());
    }

    public int getClaimedScore()
    {
        return claimedScore;
    }

    public List<Point> getSoldierPath(Point start, Point target)
    {
        return pathFinder.getSoldierPath(start, target);
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
        soldiersOnMap.getAllSoldiers()
                .filter(soldier -> !soldier.getAttackHelper().isDead())
                .forEach(soldier -> soldier.getAttackHelper().passTurn());

        turn++;
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

    public void addToClaimedResource(Resource destroyResource)
    {
        claimedResource.increase(destroyResource);
    }

    public void displayMove(Soldier soldier, Point soldierLocation, Point pointToGo)
    {
        soldiersOnLocations.move(soldier, soldierLocation, pointToGo);
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

    class PathFinder
    {
        private static final int VERTICAL_COST = 10;
        private static final int DIAGONAL_COST = 14;


        private List<MapCellNode> findPath(MapCellNode start, MapCellNode target)
        {

            MapCellNode[][] nodes = new MapCellNode[map.getSize().getWidth()][map.getSize().getHeight()];
            initialize(nodes, start, target);
            TreeSet<MapCellNode> priority = new TreeSet<>(Comparator.comparingInt(MapCellNode::getF).thenComparing(MapCellNode::getX).thenComparing(MapCellNode::getY));
            priority.add(start);
            do
            {
                MapCellNode node = priority.first();
                if (node.equals(target))
                    break;

                priority.remove(node);
                int x = node.getX();
                int y = node.getY();
                for (int i = -1; i <= 1; i++)
                    for (int j = -1; j <= 1; j++)
                        if (!(i == 0 && j == 0) &&
                                (map.isEmpty(x + i, y + j) || (target.getX() == x + i && target.getY() == y + j)) &&
                                numberOfSoldiersIn(x + i, y + j) < 5)
                        {
                            MapCellNode child = nodes[x + i][y + j];
                            if (child == null)
                            {
                                child = new MapCellNode(new Point(x + i, y + j), node, 0);
                                child.setDistEnd(getDistance(child, target));
                                nodes[x + i][y + j] = child;
                            }

                            if (child.isVisited())
                                continue;
                            int dist = (i == j || i == -j) ? DIAGONAL_COST : VERTICAL_COST;
                            if (node.getDistStart() + dist < child.getDistStart())
                            {
                                priority.remove(child);
                                child.setDistStart(node.getDistStart() + dist);
                                child.setParent(node);
                                priority.add(child);
                            }
                        }
                node.setVisited(true);
            } while (!priority.isEmpty());
            return extractPath(start, target);
        }

        private List<MapCellNode> extractPath(MapCellNode start, MapCellNode target)
        {
            LinkedList<MapCellNode> path = new LinkedList<>();
            MapCellNode current = target;
            path.add(current);
            while (!current.equals(start))
            {
                current = current.getParent();
                path.add(current);
            }
            return path;
        }

        private void initialize(MapCellNode[][] nodes, MapCellNode start, MapCellNode target)
        {
            start.setDistStart(0);
            start.setVisited(false);
            target.setVisited(false);
            target.setDistEnd(0);
            target.setDistStart(Integer.MAX_VALUE);
            nodes[start.getX()][start.getY()] = start;
            nodes[target.getX()][target.getY()] = target;
        }

        private int getDistance(MapCellNode node, MapCellNode target)
        {
            return (int)Math.sqrt((node.getX() - target.getX()) * (node.getX() - target.getX()) + (node.getY() - target.getY()) * (node.getY() - target.getY())) * 10;
        }

        public List<Point> getSoldierPath(Point soldierLocation, Point buildingLocation)
        {
            MapCellNode soldier = new MapCellNode(soldierLocation, null, 0);
            MapCellNode building = new MapCellNode(buildingLocation, null, 0);
            List<MapCellNode> path = findPath(soldier, building);
            List<Point> soldierPath = new ArrayList<>(path.size());
            for (MapCellNode aPath : path) soldierPath.add(aPath.getPoint());
            return soldierPath;
        }
    }

}
