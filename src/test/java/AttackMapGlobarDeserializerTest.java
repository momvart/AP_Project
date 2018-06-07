
import com.google.gson.*;
import models.*;
import models.attack.AttackMap;
import models.buildings.ArcherTower;
import models.buildings.Building;
import org.junit.*;
import serialization.*;

import java.io.InputStreamReader;

import static org.junit.Assert.*;

public class AttackMapGlobarDeserializerTest
{
    @Before
    public void setUp()
    {
        World.initialize();
    }

    @Test
    public void testDeserialization()
    {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(AttackMap.class, new AttackMapGlobalAdapter())
                .registerTypeAdapter(Building.class, new BuildingGlobalAdapter())
                .create();
        AttackMap map = gson.fromJson(new InputStreamReader(getClass().getClassLoader().getResourceAsStream("AttackMapTest.json")), AttackMap.class);

        assertEquals(map.getSize().getWidth(), 30);
        assertEquals(map.getSize().getHeight(), 30);

        assertEquals(map.getResources().getElixir(), 65000);
        assertEquals(map.getResources().getGold(), 100000);

        ArcherTower archerTower = (ArcherTower)map.getBuildings(ArcherTower.BUILDING_TYPE).getMin();
        assertEquals(archerTower.getLocation().getX(), 7);
        assertEquals(archerTower.getDamagePower(), 26);
    }
}
