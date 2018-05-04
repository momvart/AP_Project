import models.Map;
import models.World;
import models.buildings.ElixirMine;
import models.buildings.GoldMine;
import utils.MapCellNode;
import utils.Point;

import java.util.List;

public class PathFinderTest
{
    MapCellNode start;
    MapCellNode target;

    //    PathFinder pathFinder;
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

        start = new MapCellNode(new Point(0, 0), null, 0);
        target = new MapCellNode(new Point(7, 7), null, 0);
//        pathFinder = new PathFinder();
    }

    @org.junit.After
    public void tearDown() throws Exception
    {
    }

    @org.junit.Test
    public void findPath()
    {
        for (int i = 0; i < 1; i++)
        {
//            List<Point> soldierPath = pathFinder.getSoldierPath(World.getVillage().getMap(), start.getPoint(), target.getPoint());
//            for( Point point : soldierPath)
//                System.out.println(point.toString());
        }
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