package serialization;

import com.google.gson.*;
import models.buildings.Building;
import models.buildings.BuildingValues;
import models.buildings.Storage;
import utils.Point;

import java.lang.reflect.Type;

public class BuildingGlobalAdapter implements JsonDeserializer<Building>, JsonSerializer<Building>
{

    private static final String TYPE_FIELD_NAME = "type";
    private static final String LOCATION_X_FIELD_NAME = "x";
    private static final String LOCATION_Y_FIELD_NAME = "y";
    private static final String LEVEL_FIELD_NAME = "level";

    @Override
    public Building deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException
    {
        JsonObject obj = json.getAsJsonObject();

        Class cls = BuildingValues.getBuildingClass(obj.get(TYPE_FIELD_NAME).getAsInt());

        Point location = new Point(obj.get(LOCATION_X_FIELD_NAME).getAsInt(), obj.get(LOCATION_Y_FIELD_NAME).getAsInt());

        Building building = context.deserialize(json, cls);
        building.setLocation(location);

        building.ensureLevel();

        return building;
    }


    @Override
    public JsonElement serialize(Building src, Type typeOfSrc, JsonSerializationContext context)
    {
        JsonObject retVal = new JsonObject();
        retVal.add(TYPE_FIELD_NAME, new JsonPrimitive(src.getType()));
        retVal.add(LEVEL_FIELD_NAME, new JsonPrimitive(src.getLevel()));
        retVal.add(LOCATION_X_FIELD_NAME, new JsonPrimitive(src.getLocation().getX()));
        retVal.add(LOCATION_Y_FIELD_NAME, new JsonPrimitive(src.getLocation().getY()));

        if (src instanceof Storage)
            retVal.add("amount", new JsonPrimitive(((Storage)src).getCurrentAmount()));
        return retVal;
    }
}
