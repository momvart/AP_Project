import models.Map;
import models.World;
import models.buildings.ElixirMine;
import models.buildings.GoldMine;
import utils.MapCellNode;
import utils.PathFinder;
import utils.Point;

import java.util.List;

import org.junit.*;

public class PathFinderTest
{
    PathFinder pathFinder;
    MapCellNode start;
    MapCellNode target;

    @org.junit.Before
    public void setUp() throws Exception
    {
        World.initialize();
        World.newGame();
        World.getVillage().getMap().addBuilding(new GoldMine(new Point(5, 8), 2));
        World.getVillage().getMap().addBuilding(new GoldMine(new Point(6, 8), 3));
        World.getVillage().getMap().addBuilding(new GoldMine(new Point(7, 8), 4));
        World.getVillage().getMap().addBuilding(new GoldMine(new Point(8, 8), 5));
        World.getVillage().getMap().addBuilding(new GoldMine(new Point(9, 8), 6));
        World.getVillage().getMap().addBuilding(new GoldMine(new Point(10, 8), 7));
        World.getVillage().getMap().addBuilding(new GoldMine(new Point(11, 8), 8));
        World.getVillage().getMap().addBuilding(new GoldMine(new Point(12, 8), 9));
        World.getVillage().getMap().addBuilding(new GoldMine(new Point(13, 8), 10));
        World.getVillage().getMap().addBuilding(new GoldMine(new Point(14, 8), 11));
        World.getVillage().getMap().addBuilding(new GoldMine(new Point(15, 8), 12));
        World.getVillage().getMap().addBuilding(new GoldMine(new Point(16, 8), 13));
        World.getVillage().getMap().addBuilding(new GoldMine(new Point(17, 8), 14));
        World.getVillage().getMap().addBuilding(new GoldMine(new Point(18, 8), 15));

        World.getVillage().getMap().addBuilding(new ElixirMine(new Point(5, 18), 2));
        World.getVillage().getMap().addBuilding(new ElixirMine(new Point(6, 18), 3));
        World.getVillage().getMap().addBuilding(new ElixirMine(new Point(7, 18), 4));
        World.getVillage().getMap().addBuilding(new ElixirMine(new Point(8, 18), 5));
        World.getVillage().getMap().addBuilding(new ElixirMine(new Point(9, 18), 6));
        World.getVillage().getMap().addBuilding(new ElixirMine(new Point(10, 18), 7));
        World.getVillage().getMap().addBuilding(new ElixirMine(new Point(11, 18), 8));
        World.getVillage().getMap().addBuilding(new ElixirMine(new Point(12, 18), 9));
        World.getVillage().getMap().addBuilding(new ElixirMine(new Point(13, 18), 10));
        World.getVillage().getMap().addBuilding(new ElixirMine(new Point(14, 18), 11));
        World.getVillage().getMap().addBuilding(new ElixirMine(new Point(15, 18), 12));
        World.getVillage().getMap().addBuilding(new ElixirMine(new Point(16, 18), 13));
        World.getVillage().getMap().addBuilding(new ElixirMine(new Point(17, 18), 14));
        World.getVillage().getMap().addBuilding(new ElixirMine(new Point(18, 18), 15));

        PathFinder pathFinder = new PathFinder(World.getVillage().getMap());
        this.pathFinder = pathFinder;
        start = new MapCellNode(new Point(0, 0), null, 0);
        target = new MapCellNode(new Point(15, 23), null, 0);
    }

    @org.junit.After
    public void tearDown() throws Exception
    {
    }

    @org.junit.Test
    public void findPath()
    {
        List<MapCellNode> path = pathFinder.findPath(start, target);

//        boolean found = false;
//        for (int i = 0; i < World.getVillage().getMap().getSize().getWidth(); i++)
//        {
//            for (int j = 0; j < World.getVillage().getMap().getSize().getHeight(); j++)
//            {
//                for (int k = 0; k < path.size(); k++)
//                {
//                    if (i == path.get(k).getPoint().getX() && j == path.get(k).getPoint().getY())
//                        found = true;
//                }
//                if (i == target.getPoint().getX() && j == target.getPoint().getY())
//                {
//                    System.out.print("T");
//                    found = false;
//                }
//                else if (found)
//                {
//                    System.out.print("*");
//                    found = false;
//                }
//                else if (i == start.getPoint().getX() && j == start.getPoint().getY())
//                    System.out.print("S");
//                else if (World.getVillage().getMap().isEmptyForBuilding(i, j))
//                    System.out.print("-");
//                else
//                    System.out.print("#");
//            }
//            System.out.println();
//        }
        printMap(path, "hazrat");
    }

    @Test
    public void testFindPath2()
    {
        List<MapCellNode> path = pathFinder.findPath2(start, target);

        printMap(path, "me");
    }

    private void printMap(List<MapCellNode> path, String text)
    {
        System.out.println(text);

        System.out.println("Total steps: " + path.size());

        Map map = World.getVillage().getMap();
        char[][] print = new char[map.getSize().getWidth()][map.getSize().getWidth()];
        for (MapCellNode node : path)
            print[node.getX()][node.getY()] = '*';
        print[start.getX()][start.getY()] = 'S';
        print[target.getX()][target.getY()] = 'T';

        for (int j = 0; j < print.length; j++)
        {
            for (int i = 0; i < print.length; i++)
                if (!map.isEmpty(i, j))
                    System.out.print('#');
                else if (print[i][j] != Character.MIN_VALUE)
                    System.out.print(print[i][j]);
                else
                    System.out.print('-');
            System.out.print('\n');
        }
    }
}