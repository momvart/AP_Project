package utils;

import models.Map;
import models.buildings.Building;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeSet;

public class PathFinder
{
    private static final int VERTICAL_COST = 10;
    private static final int DIAGONAL_COST = 14;



    public List<MapCellNode> findPath(Map map, MapCellNode start, MapCellNode target)
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
                    if (!(i == 0 && j == 0) && map.isEmpty(x + i, y + j))
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
}
