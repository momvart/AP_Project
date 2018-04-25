package serialization;

import com.google.gson.*;
import models.soldiers.Soldier;
import models.soldiers.SoldierValues;

import java.lang.reflect.Type;

public class SoldierAdapter implements JsonSerializer<Soldier>, JsonDeserializer<Soldier>
{

    private static final String TYPE_FIELD_NAME = "type";

    @Override
    public Soldier deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException
    {
        Class cls = SoldierValues.getSoldierClass(json.getAsJsonObject().get(TYPE_FIELD_NAME).getAsInt());
        return context.deserialize(json, cls);
    }

    @Override
    public JsonElement serialize(Soldier src, Type typeOfSrc, JsonSerializationContext context)
    {
        JsonObject retVal = context.serialize(src).getAsJsonObject();
        retVal.add(TYPE_FIELD_NAME, new JsonPrimitive(src.getType()));
        return retVal;
    }
}
