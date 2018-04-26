import models.World;
import utils.Node;
import utils.PathFinder;
import utils.Point;

import java.util.List;

public class PathFinderTest
{
    PathFinder pathFinder;

    @org.junit.Before
    public void setUp() throws Exception
    {
        World.initialize();
        World.newGame();
        PathFinder pathFinder = new PathFinder(World.getVillage().getMap());
        this.pathFinder = pathFinder;

    }

    @org.junit.After
    public void tearDown() throws Exception
    {
    }

    @org.junit.Test
    public void findPath()
    {
        Node start = new Node(new Point(1, 1), null, 0, 0);
        Node target = new Node(new Point(28, 19), null, 0, 0);
        List<Node> path = pathFinder.findPath(start, target);
        boolean found = false;
        for (int i = 0; i < World.getVillage().getMap().getSize().getWidth(); i++)
        {
            for (int j = 0; j < World.getVillage().getMap().getSize().getHeight(); j++)
            {
                for (int k = 0; k < path.size(); k++)
                {
                    if (i == path.get(k).getPoint().getX() && j == path.get(k).getPoint().getY())
                        found = true;
                }
                if (i == target.getPoint().getX() && j == target.getPoint().getY())
                {
                    System.out.print("T");
                    found = false;
                }
                else if (found)
                {
                    System.out.print("*");
                    found = false;
                }
                else if (i == start.getPoint().getX() && j == start.getPoint().getY())
                    System.out.print("S");
                else if (World.getVillage().getMap().isEmpty(i, j))
                    System.out.print("-");
                else
                    System.out.print("#");
            }
            System.out.println();
        }
    }
}