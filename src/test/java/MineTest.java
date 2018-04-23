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
        // TODO: 4/21/18 : check more tests
        goldMine.passTurn();
        goldMine.passTurn();
        elixirMine.passTurn();
        elixirMine.passTurn();
        assertEquals(goldStorage.getCurrentAmount(),0);
        assertEquals(elixirStorage.getCurrentAmount(),0);;
        assertEquals(elixirMine.getMinedResources(),10);
        assertEquals(goldMine.getMinedResources(),20);
        goldMine.mine();
        elixirMine.mine();
        assertEquals(goldStorage.getCurrentAmount(),20);
        assertEquals(elixirStorage.getCurrentAmount(),10);;
        assertEquals(elixirMine.getMinedResources(),0);
        assertEquals(goldMine.getMinedResources(),0);
        goldMine.passTurn();
        goldMine.passTurn();
        goldMine.passTurn();
        elixirMine.passTurn();
        elixirMine.passTurn();
        elixirMine.passTurn();
        assertEquals(goldStorage.getCurrentAmount(),20);
        assertEquals(elixirStorage.getCurrentAmount(),10);;
        assertEquals(elixirMine.getMinedResources(),15);
        assertEquals(goldMine.getMinedResources(),30);
        goldMine.mine();
        elixirMine.mine();
        assertEquals(goldStorage.getCurrentAmount(),50);
        assertEquals(elixirStorage.getCurrentAmount(),20);;
        assertEquals(elixirMine.getMinedResources(),0);
        assertEquals(goldMine.getMinedResources(),0);

    }
}