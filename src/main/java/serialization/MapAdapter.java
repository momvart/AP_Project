package serialization;

import com.google.gson.*;
import com.google.gson.reflect.*;
import models.Map;
import models.buildings.*;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class MapAdapter implements JsonDeserializer<Map>, JsonSerializer<Map>
{
    private static final String BUILDINGS_FIELD_NAME = "buildings";

    private TypeAdapter<Map> defAdapter;

    public MapAdapter()
    {
        this.defAdapter = new Gson().getAdapter(Map.class);
    }

    @Override
    public Map deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException
    {
        Map map = defAdapter.fromJsonTree(json);

        map.setUpBuildingsLists(context.deserialize(json.getAsJsonObject().get(BUILDINGS_FIELD_NAME), new TypeToken<ArrayList<Building>>() { }.getType()));

        return map;
    }

    @Override
    public JsonElement serialize(Map src, Type typeOfSrc, JsonSerializationContext context)
    {
        JsonElement json = defAdapter.toJsonTree(src);
        json.getAsJsonObject().add(BUILDINGS_FIELD_NAME, context.serialize(src.getBuildings(), new TypeToken<ArrayList<Building>>() { }.getType()));
        return json;
    }
}
