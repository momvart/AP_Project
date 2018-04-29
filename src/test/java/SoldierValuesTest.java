import exceptions.ConsoleRuntimeException;
import models.World;
import models.soldiers.*;
import org.junit.*;

import static org.junit.Assert.*;

import static models.soldiers.SoldierValues.getTypeByName;

public class SoldierValuesTest
{
    @Before
    public void setUp()
    {
        World.initialize();
    }

    @Test(expected = ConsoleRuntimeException.class)
    public void getTypeByNameTest()
    {
        assertEquals(SoldierValues.getTypeByName("archer"), Archer.SOLDIER_TYPE);
        assertEquals(SoldierValues.getTypeByName("guardian"), Guardian.SOLDIER_TYPE);
        SoldierValues.getTypeByName("salam");
    }
}
