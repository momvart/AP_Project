package utils;

import models.Map;
import models.buildings.Building;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeSet;

public class PathFinder
{
    private static final double VERTICAL_COST = 1;
    private static final double DIAGONAL_COST = 1.41;

    private static final int VERTICAL_COST_INT = 10;
    private static final int DIAGONAL_COST_INT = 14;

    List<MapCellNode> openList = new LinkedList<>();
    List<MapCellNode> closedList = new LinkedList<>();
    List<MapCellNode> result = new LinkedList<>();
    Map map;
    Building[][] buildings;
    MapCellNode[][] nodes;

    public PathFinder(Map map)
    {
        this.map = map;
    }


    public List<MapCellNode> findPath2(MapCellNode start, MapCellNode target)
    {
        initialize(start, target);
        TreeSet<MapCellNode> priority = new TreeSet<>(Comparator.comparingDouble(MapCellNode::getF).thenComparing(MapCellNode::getDistStart).thenComparing(MapCellNode::getDistEnd).thenComparing(MapCellNode::getX).thenComparing(MapCellNode::getY));
        start.setDistStart(0);
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
                    if (!(i == 0 && j == 0) && map.isEmpty(x + i, y + j))
                    {
                        MapCellNode child = nodes[x + i][y + j];
                        if (child.isVisited())
                            continue;
                        double dist = (i == j || i == -j) ? DIAGONAL_COST : VERTICAL_COST;
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

    public List findPath(MapCellNode start, MapCellNode target)
    {
        initialize(start, target);
        start.setDistStart(0);
        openList.add(start);
        MapCellNode lastNode;
        while (true)
        {
            openList.sort(Comparator.comparingDouble(MapCellNode::getF));
            int x = openList.get(0).getX();
            int y = openList.get(0).getY();
            for (int i = -1; i <= 1; i++)
                for (int j = -1; j <= 1; j++)
                {
                    if (map.isEmptyForBuilding(i + x, j + y) && map.isValid(i + x, j + y) && !(i == 0 && j == 0) && !closedList.contains(nodes[i + x][y + j]))
                    {
                        nodes[i + x][j + y].setParent(openList.get(0));
                        if (nodes[x][y].getParent() != null)
                            nodes[i + x][j + y].setDistStart(nodes[x][y].getParent().getDistStart() + i == j || i == -j ? DIAGONAL_COST : VERTICAL_COST);
                        else
                            nodes[i + x][j + y].setDistStart(i == j || i == -j ? DIAGONAL_COST : VERTICAL_COST);
                        openList.add(nodes[i + x][j + y]);
                    }
                }
            closedList.add(openList.get(0));
            openList.remove(0);
            MapCellNode theNode = closedList.get(closedList.size() - 1);
            for (MapCellNode node : openList)
            {
                if (node.getDistStart() > theNode.getDistStart() + (theNode.getX() == node.getX() || theNode.getY() == node.getY() ? VERTICAL_COST : DIAGONAL_COST))
                {
                    node.setParent(theNode);
                    node.setDistStart(theNode.getDistStart() + (theNode.getX() == node.getX() || theNode.getY() == node.getY() ? VERTICAL_COST : DIAGONAL_COST));
                }
            }
            if (theNode.equals(target))
            {
                lastNode = theNode;
                break;
            }
        }
        while (true)
        {
            if (lastNode.equals(start))
                break;
            result.add(lastNode);
            lastNode = lastNode.getParent();
        }
        return result;
    }

    private void initialize(MapCellNode start, MapCellNode target)
    {
        nodes = new MapCellNode[map.getSize().getWidth()][map.getSize().getHeight()];
        for (int i = 0; i < map.getSize().getWidth(); i++)
            for (int j = 0; j < map.getSize().getHeight(); j++)
            {
                nodes[i][j] = new MapCellNode(new Point(i, j), null, getDistance(i, j, target));
            }
        nodes[start.getPoint().getX()][start.getPoint().getY()] = start;
        nodes[target.getPoint().getX()][target.getPoint().getY()] = target;
    }

    private double getDistance(int x, int y, MapCellNode target)
    {
        int deltaX = x - target.getX();
        int deltaY = y - target.getY();
        return (Math.sqrt(deltaX * deltaX + deltaY * deltaY));
//        return 0;
    }
}
