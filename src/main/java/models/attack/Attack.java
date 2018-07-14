package models.attack;

import exceptions.*;
import models.Resource;
import models.attack.attackHelpers.NetworkHelper;
import models.buildings.*;
import models.soldiers.MoveType;
import models.soldiers.Soldier;
import models.soldiers.SoldierCollection;
import models.soldiers.SoldierValues;
import utils.*;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class Attack
{
    private final int MAX_SOLDIER_IN_CELL = 5;
    private SoldierCollection soldiers = new SoldierCollection();
    private Resource claimedResource;
    private final Resource totalResource;
    private int claimedScore;
    private AttackMap map;
    private int turn;
    private HashMap<Storage, Resource> claimedResourceStorages = new HashMap<>();
    public boolean isReal;
    private Consumer<TimeSpan> timeChangeListener;
    private TimeSpan attackTime;
    private String defenderName;

    private SoldierCoordinatedCollection soldiersOnLocations;

    public Attack(AttackMap map, Boolean isReal)
    {
        this.map = map;
        claimedResource = new Resource(0, 0);

        int totalGold = 0, totalElixir = 0;
        for (Building storage : map.getBuildings(GoldStorage.BUILDING_TYPE))
            totalGold += ((GoldStorage)storage).getCurrentAmount();
        for (Building storage : map.getBuildings(ElixirStorage.BUILDING_TYPE))
            totalElixir += ((ElixirStorage)storage).getCurrentAmount();

        totalResource = new Resource(totalGold, totalElixir);
        map.getAllBuildings().forEach(building -> totalResource.increase(building.getBuildingInfo().getDestroyResource()));
        map.getAllBuildings().forEach(building -> building.participateIn(this));
        soldiersOnLocations = new SoldierCoordinatedCollection(map.getSize());
        this.isReal = isReal;
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

    public void addScore(int score)
    {
        this.claimedScore += score;
    }

    public int getClaimedScore()
    {
        return claimedScore;
    }

    public Resource getClaimedResource()
    {
        return claimedResource;
    }

    public void addToClaimedResource(Resource destroyResource)
    {
        claimedResource.increase(destroyResource);
    }

    public Resource getTotalResource()
    {
        return totalResource;
    }

    public TimeSpan getAttackTime()
    {
        return attackTime;
    }

    public void setAttackTime(TimeSpan attackTime)
    {
        this.attackTime = attackTime;
        callOnTimeChanged(attackTime);
        attackTime.setTimeChangeListener(this::callOnTimeChanged);
    }

    private void callOnTimeChanged(TimeSpan time)
    {
        if (timeChangeListener != null)
            timeChangeListener.accept(attackTime);
        if (isReal)
            NetworkHelper.setTime(time);
    }

    public void setTimeChangeListener(Consumer<TimeSpan> timeChangeListener)
    {
        this.timeChangeListener = timeChangeListener;
    }

    public String getDefenderName()
    {
        return defenderName;
    }

    public void setDefenderName(String defenderName)
    {
        this.defenderName = defenderName;
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

        map.getAllBuildings().forEach(building -> building.getAttackHelper().passTurn());

        turn++;
    }

    /**
     * Adds units to the list undeployed.
     */
    int soldierNum = 0;

    public Soldier getSoldierById(long id) throws SoldierNotFoundException
    {
        return soldiers.getSoldierById(id);
    }

    public void setClaimedScore(int claimedScore)
    {
        this.claimedScore = claimedScore;
    }

    public void setClaimedResource(Resource claimedResource)
    {
        this.claimedResource.setGold(claimedResource.getGold());
        this.claimedResource.setElixir(claimedResource.getElixir());
    }

    public enum QuitReason
    {
        TURN("Time Is Up!"),
        USER("User Quited The Battle!"),
        MAP_DESTROYED("All Buildings Destroyed!"),
        SOLDIERS_DIE("All Soldiers Died!");

        private String title;

        QuitReason(String title)
        {
            this.title = title;
        }

        public String getTitle()
        {
            return title;
        }
    }

    public AttackReport quitAttack(QuitReason reason)
    {
        return new AttackReport(null, null,
                totalResource, claimedResource,
                0, claimedScore,
                soldiers.getLists().stream()
                        .map(list -> (int)list.stream().filter(soldier -> soldier.isParticipating(this) && soldier.getAttackHelper().isDead()).count())
                        .collect(Collectors.toList()));
    }

    private void decreaseLootFromDefenderStorages()
    {
        for (Storage storage : claimedResourceStorages.keySet())
        {
            if (storage != null && claimedResourceStorages.get(storage) != null)
            {
                storage.decreaseCurrentAmount(claimedResourceStorages.get(storage).getElixir() + claimedResourceStorages.get(storage).getGold());
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
        return map.getAllBuildings().allMatch(building -> building.getAttackHelper().isDestroyed());
    }
    //endregion

    //region Soldiers Management

    public Building getBuildingById(long id) throws BuildingNotFoundException
    {
        List<Building> buildings = map.getAllBuildings().collect(Collectors.toList());
        for (Building building : buildings)
            if (building.getId() == id)
                return building;
        throw new BuildingNotFoundException();
    }

    public void addUnit(Soldier soldier)
    {
        soldiers.addSoldier(soldier);
        soldier.setId(soldierNum);
        soldierNum++;
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
            callOnSoldierPut(soldier);
        }
    }


    public void putUnits(int unitType, int count, Point location, boolean networkPermission) throws ConsoleException
    {
        if (!isReal && !networkPermission)
            return;
        if (!map.isMarginal(location))
            throw new ConsoleRuntimeException("Invalid location.", location + " is not a marginal location.", new IllegalArgumentException("Invalid location"));

        List<Soldier> available = getUnitsYetToBeDeployed(unitType).limit(count).collect(Collectors.toList());
        int current = numberOfSoldiersIn(location, SoldierValues.getSoldierInfo(unitType).getMoveType());

        if (MAX_SOLDIER_IN_CELL - current < count)
            throw new FilledCellException(location, "Current: " + current);
        else if (available == null || available.size() < count)
            throw new NotEnoughSoldierException(unitType, available == null ? 0 : available.size(), count);
        else
            for (int i = 0; i < count; i++)
                putUnit(available.get(i), location);
        if (isReal)
            NetworkHelper.putUnits(unitType, count, location);
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
    public Stream<Soldier> getUnitsYetToBeDeployed(int unitType)
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

        BiConsumer<Integer, Integer> checker = (Integer dx, Integer dy) ->
        {
            Point toCheck = new Point(location.getX() + dx, location.getY() + dy);
            if (toCheck.getX() < 0 || toCheck.getX() >= map.getWidth() || toCheck.getY() < 0 || toCheck.getY() >= map.getHeight())
                return;

            if (numberOfSoldiersIn(toCheck.getX(), toCheck.getY(), moveType) > 0)
                if (Point.euclideanDistance2nd(toCheck, location) < Point.euclideanDistance2nd(min, location))
                {
                    min.setX(toCheck.getX());
                    min.setY(toCheck.getY());
                }
        };

        for (int radius = 0; radius <= range; radius++)
        {
            if (radius * radius > Point.euclideanDistance2nd(min, location))
                break;

            for (int j = -radius; j <= radius; j++)
            {
                checker.accept(-radius, j);
                checker.accept(+radius, j);
            }

            for (int i = -radius + 1; i <= radius - 1; i++)
            {
                checker.accept(i, -radius);
                checker.accept(i, +radius);
            }
        }

        if (!min.equals(new Point(-30, -30)))
            return min;
        else
            throw new SoldierNotFoundException("Soldier not found", "SoldierNotFound");
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

    public static Point getLastPointOfStanding(Attack attack, int range, Point start, Point destination, boolean isFlying)
    {
        List<Point> soldierPath = attack.getSoldierPath(start, destination, isFlying);
        if (soldierPath == null || soldierPath.size() <= 1)
            return null;
        Point lastPoint = soldierPath.get(1);

        int i;
        for (i = 1; i < soldierPath.size() - 1; i++)
        {
            lastPoint = soldierPath.get(i);
            if (Point.euclideanDistance(soldierPath.get(i + 1), destination) > range)
            {
                break;
            }
        }
        return lastPoint;
    }

    public static Point getNextPathStraightReachablePoint(Attack attack, Point start, Point destination, boolean isFlying)
    {
        List<Point> soldierPath = attack.getSoldierPath(start, destination, isFlying);
        if (soldierPath == null)
            return null;
        Point pointToGo = soldierPath.get(soldierPath.size() - 1);
        ArrayList<Point> aliveBuildingPositions = getAliveBuildingsPositions(attack);

        int i;
        for (i = soldierPath.size() - 2; i >= 0; i--)
        {
            if (isThereABuildingInPath(start, soldierPath.get(i + 1), aliveBuildingPositions))
            {
                return pointToGo;
            }
            pointToGo = soldierPath.get(i + 1);
        }
        return pointToGo;
    }

    private static boolean isThereABuildingInPath(Point start, Point destination, ArrayList<Point> buildingsPositions)
    {
        ArrayList<Point> pointsOnTheLine = getPointsOnLine(start, destination);
        if (pointsOnTheLine.contains(start))
            pointsOnTheLine.remove(start);
        return pointsOnTheLine.stream().anyMatch(p -> buildingsPositions.contains(p));
    }

    private static ArrayList<Point> getAliveBuildingsPositions(Attack attack)
    {
        ArrayList<Point> positions = new ArrayList<>();
        getAliveBuildings(attack).filter(building -> building.getType() != Trap.BUILDING_TYPE).forEach(building -> positions.add(building.getLocation()));
        return positions;
    }

    public static Stream<Building> getAliveBuildings(Attack attack)
    {
        return attack.getMap().getBuildings().stream().filter(building -> !building.getAttackHelper().isDestroyed()).filter(building -> building.getAttackHelper().getStrength() > 0);
    }

    public static ArrayList<Point> getPointsOnLine(Point start, Point destination)
    {
        ArrayList<Point> pointsOnLine = new ArrayList<>();
        double stepLength = .2;
        PointF begin = new PointF(start.getX() + .5, start.getY() + .5);
        PointF end = new PointF(destination.getX() + .5, destination.getY() + .5);
        double distance = PointF.euclideanDistance(begin, end);
        double cos = (end.getX() - begin.getX()) / distance;
        double sin = (end.getY() - begin.getY()) / distance;
        PointF currentPoint = begin;
        double initialState = begin.getX() - end.getX();
        pointsOnLine.add(start);

        while (true)
        {
            if (initialState * (currentPoint.getX() - end.getX()) <= 0)
                break;
            currentPoint.setX(currentPoint.getX() + stepLength * cos);
            currentPoint.setY(currentPoint.getY() + stepLength * sin);
            Point veryCurrentPoint = new Point((int)Math.floor(currentPoint.getX()), (int)Math.floor(currentPoint.getY()));
            if (!pointsOnLine.contains(veryCurrentPoint))
                pointsOnLine.add(veryCurrentPoint);
        }
        return pointsOnLine;
    }


    //endregion

    //region Events

    private IOnSoldierPutListener soldierPutListener;

    public void setSoldierPutListener(IOnSoldierPutListener soldierPutListener)
    {
        this.soldierPutListener = soldierPutListener;
    }

    private void callOnSoldierPut(Soldier soldier)
    {
        if (soldierPutListener != null)
            soldierPutListener.onSoldierPut(soldier);
    }

    //endregion


    //region Path Finding
    private PathFinder pathFinder = new PathFinder();

    public List<Point> getSoldierPath(Point start, Point target, boolean isFlying)
    {
        return pathFinder.getSoldierPath(start, target, isFlying);
    }

    public List<Point> getSoldierPath2(Point start, Point target, boolean isFlying, int damage, int speed)
    {
        return pathFinder.getSoldierPath2(start, target, isFlying, damage, speed);
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
                                (((isFlying && map.isValid(x + i, y + j)) || map.isEmptyOrDestroyed(x + i, y + j)) || (target.getX() == x + i && target.getY() == y + j)))
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

        private List<MapCellNode> findPath2(MapCellNode start, MapCellNode target, boolean isFlying, int damage, int speed)
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
                        if (!(i == 0 && j == 0) && map.isValid(x + i, y + j))
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

                            int dist = 0; //Way cost
                            dist += Math.max(((i == j || i == -j) ? DIAGONAL_COST : VERTICAL_COST) / speed * 10, 1);
                            if (!isFlying && !map.isEmpty(x + i, y + j))
                                dist += Math.max(map.getBuildingAt(x + i, y + j).getAttackHelper().getStrength() / damage, 1);

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

        public List<Point> getSoldierPath2(Point soldierLocation, Point buildingLocation, boolean isFlying, int damage, int speed)
        {
            MapCellNode soldier = new MapCellNode(soldierLocation, null, 0);
            MapCellNode building = new MapCellNode(buildingLocation, null, 0);
            List<MapCellNode> path = findPath2(soldier, building, isFlying, damage, speed);
            List<Point> soldierPath = new ArrayList<>(path.size());
            for (MapCellNode aPath : path) soldierPath.add(aPath.getPoint());
            return soldierPath;
        }
    }

    //endregion

}
