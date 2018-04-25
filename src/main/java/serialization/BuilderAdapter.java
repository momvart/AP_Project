package serialization;

import com.google.gson.*;
import models.buildings.*;

import java.lang.reflect.Type;

public class BuilderAdapter implements JsonSerializer<Building>, JsonDeserializer<Building>
{
    private static final String TYPE_FIELD_NAME = "type";
    @Override
    public Building deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException
    {
        Class cls = BuildingValues.getBuildingClass(json.getAsJsonObject().get(TYPE_FIELD_NAME).getAsInt());
        return context.deserialize(json, cls);
    }

    @Override
    public JsonElement serialize(Building src, Type typeOfSrc, JsonSerializationContext context)
    {
        JsonObject retVal = context.serialize(src).getAsJsonObject();
        retVal.add(TYPE_FIELD_NAME, new JsonPrimitive(src.getType()));
        return retVal;
    }
}
