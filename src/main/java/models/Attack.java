package models;

import exceptions.*;
import models.buildings.Building;
import models.buildings.DefensiveTower;
import models.buildings.Storage;
import models.soldiers.MoveType;
import models.soldiers.Soldier;
import models.soldiers.SoldierCollection;
import models.soldiers.SoldierValues;
import utils.MapCellNode;
import utils.Point;
import utils.Size;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class Attack
{
    private final int MAX_SOLDIER_IN_CELL = 5;
    private SoldierCollection soldiers = new SoldierCollection();
    private Resource claimedResource;
    private int claimedScore;
    private AttackMap map;
    private int turn;
    private HashMap<Storage, Resource> gainedResourceFromStorageDestroying = new HashMap<>();

    private SoldierCoordinatedCollection soldiersOnLocations;

    public Attack(AttackMap map)
    {
        this.map = map;
        claimedResource = new Resource(0, 0);
        soldiersOnLocations = new SoldierCoordinatedCollection(map.getSize());
    }

    //region Turn, Score, Resource
    public int getTurn()
    {
        return turn;
    }

    public SoldierCoordinatedCollection getSoldiersOnLocations()
    {
        return soldiersOnLocations;
    }

    public void addScore(int score) { this.claimedScore += score; }

    public int getClaimedScore()
    {
        return claimedScore;
    }

    public Resource getClaimedResource()
    {
        return claimedResource;
    }

    public void addToGainedResourceOfStorageDesroying(Storage storage, Resource resource)
    {
        if (gainedResourceFromStorageDestroying.get(resource) != null)
        {
            gainedResourceFromStorageDestroying.put(storage, addTwo(gainedResourceFromStorageDestroying.get(resource), resource));
        }
        else
        {
            gainedResourceFromStorageDestroying.put(storage, resource);
        }
    }

    private Resource addTwo(Resource resource1, Resource resource2)
    {
        return new Resource(resource1.getGold() + resource2.getGold(), resource1.getElixir() + resource2.getElixir());
    }


    public void addToClaimedResource(Resource destroyResource)
    {
        claimedResource.increase(destroyResource);
    }
    //endregion

    public AttackMap getMap()
    {
        return map;
    }

    public void passTurn()
    {
        getDeployedAliveUnits()
                .forEach(soldier -> soldier.getAttackHelper().passTurn());

        map.getAllDefensiveTowers().stream()
                .filter(defensiveTower -> !defensiveTower.isDestroyed())
                .forEach(defensiveTower -> defensiveTower.attack(this));

        turn++;
    }

    public enum QuitReason
    {
        TURN,
        USER,
        MAP_DESTROYED,
        SOLDIERS_DIE
    }

    public void quitAttack(QuitReason reason)
    {
        for (int i = 1; i < SoldierValues.SOLDIER_TYPES_COUNT; i++)
            World.getVillage().getSoldiers(i).removeIf(soldier -> soldier.isParticipating(this) && soldier.getAttackHelper().isDead());
        World.getVillage().addResource(claimedResource);
        decreaseLootFromDefenderStorages();
        World.sCurrentGame.addScore(claimedScore);
    }

    private void decreaseLootFromDefenderStorages()
    {
        for (Storage storage : gainedResourceFromStorageDestroying.keySet())
        {
            if (storage != null && gainedResourceFromStorageDestroying.get(storage) != null)
            {
                storage.decreaseCurrentAmount(gainedResourceFromStorageDestroying.get(storage).getElixir() + gainedResourceFromStorageDestroying.get(storage).getGold());
            }
        }
    }

    //region Towers Management

    public List<DefensiveTower> getAllTowers()
    {
        return map.getAllDefensiveTowers();
    }

    public List<DefensiveTower> getTowers(int towerType)
    {
        return map.getDefensiveTowers(towerType);
    }

    public boolean areBuildingsDestroyed()
    {
        return map.getAllBuildings().allMatch(Building::isDestroyed);
    }
    //endregion

    //region Soldiers Management

    /**
     * Adds units to the list undeployed.
     */
    public void addUnit(Soldier soldier)
    {
        soldiers.addSoldier(soldier);
        soldier.participateIn(this);
    }

    public void addUnits(List<Soldier> soldierList)
    {
        soldierList.forEach(this::addUnit);
    }

    private void putUnit(Soldier soldier, Point location)
    {
        if (map.isValid(location))
        {
            soldier.setLocation(location);
            soldier.getAttackHelper().setSoldierIsDeployed(true);
            soldiersOnLocations.push(soldier, location);
        }
    }

    public void putUnits(int unitType, int count, Point location) throws ConsoleException
    {
        if (!map.isMarginal(location))
            throw new ConsoleRuntimeException("Invalid location.", location + " is not a marginal location.", new IllegalArgumentException("Invalid location"));

        List<Soldier> available = getUnitsInToBeDeployed(unitType).limit(count).collect(Collectors.toList());
        int current = numberOfSoldiersIn(location, SoldierValues.getSoldierInfo(unitType).getMoveType());

        if (MAX_SOLDIER_IN_CELL - current < count)
            throw new FilledCellException(location, "Current: " + current);
        else if (available == null || available.size() < count)
            throw new NotEnoughSoldierException(unitType, available == null ? 0 : available.size(), count);
        else
            for (int i = 0; i < count; i++)
                putUnit(available.get(i), location);

    }

    public int numberOfSoldiersIn(int x, int y)
    {
        return (int)soldiersOnLocations.getSoldiers(x, y).stream().filter(soldier -> !soldier.getAttackHelper().isDead()).count();
    }

    public int numberOfSoldiersIn(int x, int y, MoveType moveType)
    {
        return (int)soldiersOnLocations.getSoldiers(x, y, moveType).count();
    }

    public int numberOfSoldiersIn(Point location)
    {
        return numberOfSoldiersIn(location.getX(), location.getY());
    }

    public int numberOfSoldiersIn(Point location, MoveType moveType)
    {
        return (int)soldiersOnLocations.getSoldiers(location.getX(), location.getY(), moveType).count();
    }

    /**
     * @param unitType The soldier type that should be found.
     * @return Soldiers that are not deployed yet
     */
    public Stream<Soldier> getUnitsInToBeDeployed(int unitType)
    {
        return soldiers.getSoldiers(unitType).stream().filter(s -> !s.getAttackHelper().isSoldierDeployed());
    }

    public Stream<Soldier> getAliveUnits(int unitType)
    {
        return soldiers.getSoldiers(unitType).stream()
                .filter(soldier -> soldier != null && !soldier.getAttackHelper().isDead());
    }

    public Stream<Soldier> getDeployedAliveUnits(int unitType)
    {
        return getDeployedAliveUnits()
                .filter(soldier -> soldier.getType() == unitType);
    }

    public Stream<Soldier> getDeployedAliveUnits()
    {
        return soldiers.getAllSoldiers()
                .filter(soldier -> soldier.getAttackHelper().isSoldierDeployed())
                .filter(soldier -> !soldier.getAttackHelper().isDead());
    }

    public ArrayList<Soldier> getAllUnits(int unitType)
    {
        return soldiers.getSoldiers(unitType);
    }

    public Stream<Soldier> getAllUnits()
    {
        return soldiers.getAllSoldiers();
    }

    public boolean areSoldiersDead()
    {
        return getAllUnits().findAny().isPresent() && getAllUnits().allMatch(soldier -> soldier.getAttackHelper().isDead());
    }

    public List<Soldier> getSoldiersInRange(Point location, int range, MoveType moveType) throws SoldierNotFoundException
    {
        List<Soldier> soldiers = new ArrayList<>();
        Point point;
        int x = location.getX();
        int y = location.getY();
        for (int k = 1; k <= range; k++)
            for (int i = -1; x + k * i >= 0 && x + k * i < map.getSize().getWidth() && i <= 1; i++)
                for (int j = -1; y + k * j >= 0 && y + k * j < map.getSize().getHeight() && j <= 1; j++)
                {
                    if (i == 0 && j == 0)
                        continue;
                    if (moveType != null)
                    {
                        if (numberOfSoldiersIn(x + k * i, y + k * j, moveType) > 0)
                        {
                            soldiers.addAll(soldiersOnLocations.getSoldiers(x + k * i, y + k * j, moveType).collect(Collectors.toList()));
                        }
                    }
                    else if (numberOfSoldiersIn(x + k * i, y + k * j) > 0)
                    {
                        soldiers.addAll(soldiersOnLocations.getSoldiers(x + k * i, y + k * j));
                    }
                }
        if (soldiers.isEmpty())
            throw new SoldierNotFoundException("Soldier not found", "SoldierNotFound");
        return soldiers;
    }

    public Point getNearestSoldier(Point location, int range, MoveType moveType) throws SoldierNotFoundException
    {
        Point min = new Point(-30, -30);
        Point point;
        int x = location.getX();
        int y = location.getY();
        outer:
        for (int k = 1; k <= range; k++)
            for (int i = -1; x + k * i >= 0 && x + k * i < map.getSize().getWidth() && i <= 1; i++)
                for (int j = -1; y + k * j >= 0 && y + k * j < map.getSize().getHeight() && j <= 1; j++)
                {
                    if (i == 0 && j == 0)
                        continue;
                    if (moveType != null)
                    {
                        if (numberOfSoldiersIn(x + k * i, y + k * j, moveType) > 0)
                        {
                            point = new Point(x + k * i, y + k * j);
                            if (Point.euclideanDistance2nd(point, location) > Point.euclideanDistance2nd(min, location))
                                break outer;
                            else
                                min = new Point(point.getX(), point.getY());
                        }
                    }
                    else if (numberOfSoldiersIn(x + k * i, y + k * j) > 0)
                    {
                        point = new Point(x + k * i, y + k * j);
                        if (Point.euclideanDistance2nd(point, location) > Point.euclideanDistance2nd(min, location))
                            break outer;
                        else
                            min = new Point(point.getX(), point.getY());
                    }

                }
        if (!min.equals(new Point(-30, -30)) && Point.euclideanDistance(location, min) <= range)
            return min;
        else throw new SoldierNotFoundException("Soldier not found", "SoldierNotFound");
    }

    public void moveOnLocation(Soldier soldier, Point soldierLocation, Point pointToGo)
    {
        soldiersOnLocations.move(soldier, soldierLocation, pointToGo);
    }

    public static class SoldierCoordinatedCollection
    {
        private ArrayList<LinkedList<Soldier>> soldiers;

        private Size size;

        public SoldierCoordinatedCollection(Size mapSize)
        {
            this.size = mapSize;
            soldiers = Stream.<LinkedList<Soldier>>generate(LinkedList::new).limit(size.getWidth() * size.getHeight()).collect(Collectors.toCollection(ArrayList::new));
        }

        public LinkedList<Soldier> getSoldiers(int x, int y)
        {
            return soldiers.get(y * size.getWidth() + x);
        }

        public List<Soldier> getSoldiers(Point location)
        {
            return getSoldiers(location.getX(), location.getY());
        }

        public Stream<Soldier> getSoldiers(Point location, MoveType moveType)
        {
            return getSoldiers(location.getX(), location.getY(), moveType);
        }

        public Stream<Soldier> getSoldiers(int x, int y, MoveType moveType)
        {
            if (getSoldiers(x, y).size() == 0)
                return Stream.empty();
            if (moveType == null)
                return getSoldiers(x, y).stream();
            Iterator<Soldier> iterator;
            if (moveType == MoveType.GROUND)
                iterator = getSoldiers(x, y).iterator();
            else
                iterator = getSoldiers(x, y).descendingIterator();
            return StreamSupport.stream(Spliterators.spliteratorUnknownSize(iterator, Spliterator.ORDERED), false)
                    .filter(soldier -> soldier.getMoveType() == moveType);
        }

        public void push(Soldier soldier, int x, int y)
        {
            if (soldier.getMoveType() == MoveType.GROUND)
                getSoldiers(x, y).addFirst(soldier);
            else
                getSoldiers(x, y).addLast(soldier);
        }

        public void push(Soldier soldier, Point location)
        {
            push(soldier, location.getX(), location.getY());
        }

        public boolean pull(Soldier soldier, int x, int y)
        {
            return getSoldiers(x, y).removeFirstOccurrence(soldier);
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
    //endregion

    //region Path Finding
    private PathFinder pathFinder = new PathFinder();

    public List<Point> getSoldierPath(Point start, Point target, boolean isFlying)
    {
        return pathFinder.getSoldierPath(start, target, isFlying);
    }

    class PathFinder
    {
        private static final int VERTICAL_COST = 10;
        private static final int DIAGONAL_COST = 14;


        private List<MapCellNode> findPath(MapCellNode start, MapCellNode target, boolean isFlying)
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
                                (((isFlying && map.isValid(x + i, y + j)) || map.isEmpty(x + i, y + j)) || (target.getX() == x + i && target.getY() == y + j)))
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

        public List<Point> getSoldierPath(Point soldierLocation, Point buildingLocation, boolean isFlying)
        {
            MapCellNode soldier = new MapCellNode(soldierLocation, null, 0);
            MapCellNode building = new MapCellNode(buildingLocation, null, 0);
            List<MapCellNode> path = findPath(soldier, building, isFlying);
            List<Point> soldierPath = new ArrayList<>(path.size());
            for (MapCellNode aPath : path) soldierPath.add(aPath.getPoint());
            return soldierPath;
        }
    }

    //endregion

}
