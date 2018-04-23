package serialization;

import com.google.gson.*;

import java.lang.reflect.Type;

public class ClassAdapter implements JsonSerializer<Class>, JsonDeserializer<Class>
{
    @Override
    public Class deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException
    {
        try
        {
            return Class.forName(json.getAsJsonObject().get("name").getAsString());
        }
        catch (ClassNotFoundException ex)
        {
            return null;
        }
    }

    @Override
    public JsonElement serialize(Class src, Type typeOfSrc, JsonSerializationContext context)
    {
        JsonObject retVal = new JsonObject();
        retVal.add("name", new JsonPrimitive(src.getTypeName()));
        return retVal;
    }
}
