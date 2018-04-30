package serialization;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import models.AttackMap;
import models.Resource;
import models.buildings.Building;
import utils.Size;

import java.lang.reflect.Type;
import java.util.ArrayList;


public class AttackMapGlobalAdapter implements JsonDeserializer<AttackMap>
{
    public static final String RESOURCES_FIELD_NAME = "resources";
    public static final String SIZE_FIELD_NAME = "size";
    public static final String BUILDINGS_FIELD_NAME = "buildings";

    @Override
    public AttackMap deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException
    {
        JsonObject obj = json.getAsJsonObject();

        JsonArray jsonSize = obj.getAsJsonArray(SIZE_FIELD_NAME);
        Size size = new Size(jsonSize.get(0).getAsInt(), jsonSize.get(1).getAsInt());

        Resource resource = context.deserialize(obj.get(RESOURCES_FIELD_NAME), Resource.class);

        AttackMap map = new AttackMap(size, resource);
        map.setUpBuildingsLists(context.deserialize(json.getAsJsonObject().get(BUILDINGS_FIELD_NAME), new TypeToken<ArrayList<Building>>() { }.getType()));

        return map;
    }
}
