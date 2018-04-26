package utils;

import models.Map;
import models.Village;
import models.buildings.Building;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

public class PathFinder
{
    private static final int VERTICAL_COST = 10;
    private static final int DIAGONAL_COST = 14;
    List<Node> openList = new LinkedList<>();
    List<Node> closedList = new LinkedList<>();
    List<Node> result = new LinkedList<>();
    Map map;
    Building[][] buildings;
    Node[][] nodes;

    public PathFinder(Map map)
    {
        this.map = map;
    }

    public List findPath(Node start, Node target)
    {
        initialize(start, target);
        openList.add(start);
        Node lastNode;
        while (true)
        {
            openList.sort(Comparator.comparingInt(Node::getF));
            int x = openList.get(0).getPoint().x;
            int y = openList.get(0).getPoint().y;
            for (int i = -1; i <= 1; i++)
                for (int j = -1; j <= 1; j++)
                {
                    if (map.isEmpty(i + x, j + y) && map.isValid(i + x, j + y) && !(i == 0 && j == 0) && !closedList.contains(nodes[i + x][y + j]))
                    {
                        nodes[i + x][j + y].setParent(openList.get(0));
                        if (nodes[x][y].getParent() != null)
                            nodes[i + x][j + y].setG(nodes[x][y].getParent().getG() + i == j || i == -j ? DIAGONAL_COST : VERTICAL_COST);
                        else
                            nodes[i + x][j + y].setG(i == j || i == -j ? DIAGONAL_COST : VERTICAL_COST);
                        openList.add(nodes[i + x][j + y]);
                    }
                }
            closedList.add(openList.get(0));
            openList.remove(0);
            Node theNode = closedList.get(closedList.size() - 1);
            for (Node node : openList)
            {
                if (node.getG() > theNode.getG() + (theNode.getPoint().x == node.getPoint().x || theNode.getPoint().y == node.getPoint().y ? VERTICAL_COST : DIAGONAL_COST))
                {
                    node.setParent(theNode);
                    node.setG(theNode.getG() + (theNode.getPoint().x == node.getPoint().x || theNode.getPoint().y == node.getPoint().y ? VERTICAL_COST : DIAGONAL_COST));
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

    private void initialize(Node start, Node target)
    {
        nodes = new Node[map.getSize().getWidth()][map.getSize().getHeight()];
        for (int i = 0; i < map.getSize().getWidth(); i++)
            for (int j = 0; j < map.getSize().getHeight(); j++)
            {
                nodes[i][j] = new Node(new Point(i, j), null, 0, getDistance(i, j, target));
            }
        nodes[start.getPoint().getX()][start.getPoint().getY()] = start;
        nodes[target.getPoint().getX()][target.getPoint().getY()] = target;
    }

    private int getDistance(int x, int y, Node target)
    {
        return Math.abs(x - target.getPoint().x) + Math.abs(y - target.getPoint().y);
    }
}
