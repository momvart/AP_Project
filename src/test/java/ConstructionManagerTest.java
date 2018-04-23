import exceptions.ConsoleException;
import exceptions.NoAvailableBuilderException;
import models.ConstructionManager;
import models.World;
import models.buildings.Construction;
import utils.Point;
import org.junit.*;
import static org.junit.Assert.*;

public class ConstructionManagerTest
{
    ConstructionManager constructionManager;

    @org.junit.Before
    public void setUp() throws Exception
    {
        World.initialize();
        World.newGame();
        constructionManager = World.sCurrentGame.getVillage().getConstructionManager();
    }

    @org.junit.After
    public void tearDown() throws Exception
    {
        for(Construction construction : constructionManager.getConstructions())
        {
            System.out.println(construction.toString());
            System.out.println(construction.getBuilderNum());
            System.out.println(construction.isFinished());

        }
    }

    @org.junit.Test
    public void firstTestconstruct() throws NoAvailableBuilderException
    {

        constructionManager.construct(1,new Point(2,3));

    }
    @org.junit.Test
    public void secondTestconstruct() throws NoAvailableBuilderException
    {
        constructionManager.construct(6,new Point(4,5));
    }
}