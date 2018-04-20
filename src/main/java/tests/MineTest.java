package tests;

import models.World;
import models.buildings.ElixirMine;
import models.buildings.ElixirStorage;
import models.buildings.GoldMine;
import models.buildings.GoldStorage;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import utils.Point;

import static org.junit.Assert.*;

public class MineTest
{
    GoldStorage goldStorage;
    ElixirStorage elixirStorage;
    GoldMine goldMine ;
    ElixirMine elixirMine ;
    @Before
    public void setUp() throws Exception
    {
        World.initialize();
        World.newGame();
        goldStorage = new GoldStorage(new Point(3,4));
        elixirStorage = new ElixirStorage(new Point(4,5));
        goldMine = new GoldMine(new Point(5,6));
        elixirMine = new ElixirMine(new Point(6,7));
        World.sCurrentGame.getVillage().getMap().getStorages().add(goldStorage);
        World.sCurrentGame.getVillage().getMap().getStorages().add(elixirStorage);
        World.sCurrentGame.getVillage().getMap().getBuildings().add(goldStorage);
        World.sCurrentGame.getVillage().getMap().getBuildings().add(elixirStorage);
        World.sCurrentGame.getVillage().getMap().getBuildings().add(goldMine);
        World.sCurrentGame.getVillage().getMap().getBuildings().add(elixirMine);

    }

    @After
    public void tearDown() throws Exception
    {
    }

    @Test
    public void mine()
    {
        System.out.println(goldStorage.toString());
        System.out.println(elixirStorage.toString());
        System.out.println(goldMine.toString());
        System.out.println(elixirMine.toString());
        System.out.println();
        System.out.println(goldMine.getResourceAddPerDeltaT());
        System.out.println(elixirMine.getResourceAddPerDeltaT());
        goldMine.passTurn();
        elixirMine.passTurn();
        System.out.println(goldMine.getMinedResources());
        System.out.println(elixirMine.getMinedResources());
        goldMine.mine();
        elixirMine.mine();
        System.out.println(goldMine.getMinedResources());
        System.out.println(elixirMine.getMinedResources());
        elixirMine.passTurn();
        goldMine.passTurn();
        elixirMine.passTurn();
        goldMine.passTurn();
        elixirMine.passTurn();
        goldMine.passTurn();

        System.out.println(goldMine.getMinedResources());
        System.out.println(elixirMine.getMinedResources());
    }
}