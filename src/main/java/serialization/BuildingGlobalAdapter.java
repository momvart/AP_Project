package serialization;

import com.google.gson.*;
import models.buildings.Building;
import models.buildings.BuildingValues;
import utils.Point;

import java.lang.reflect.Type;

public class BuildingGlobalAdapter implements JsonDeserializer<Building>
{
    @Override
    public Building deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException
    {
        JsonObject obj = json.getAsJsonObject();

        Class cls = BuildingValues.getBuildingClass(obj.get("type").getAsInt());

        Point location = new Point(obj.get("x").getAsInt(), obj.get("y").getAsInt());

        Building building = context.deserialize(json, cls);
        building.setLocation(location);

        building.ensureLevel();

        return building;
    }
}
