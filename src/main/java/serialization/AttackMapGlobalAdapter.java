package serialization;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import models.attack.AttackMap;
import models.Resource;
import models.buildings.Building;
import models.buildings.Wall;
import utils.Size;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class AttackMapGlobalAdapter implements JsonDeserializer<AttackMap>
{
    private static final String RESOURCES_FIELD_NAME = "resources";
    private static final String SIZE_FIELD_NAME = "size";
    private static final String BUILDINGS_FIELD_NAME = "buildings";
    private static final String WALLS_FIELD_NAME = "walls";


    @Override
    public AttackMap deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException
    {
        JsonObject obj = json.getAsJsonObject();

        JsonArray jsonSize = obj.getAsJsonArray(SIZE_FIELD_NAME);
        Size size = new Size(jsonSize.get(0).getAsInt(), jsonSize.get(1).getAsInt());

        Resource resource = context.deserialize(obj.get(RESOURCES_FIELD_NAME), Resource.class);

        AttackMap map = new AttackMap(size, resource);

        obj.get(WALLS_FIELD_NAME).getAsJsonArray().forEach(element -> element.getAsJsonObject().addProperty("type", Wall.BUILDING_TYPE));
        obj.get(BUILDINGS_FIELD_NAME).getAsJsonArray().addAll(obj.get(WALLS_FIELD_NAME).getAsJsonArray());
        map.setUpBuildingsLists(context.deserialize(json.getAsJsonObject().get(BUILDINGS_FIELD_NAME), new TypeToken<ArrayList<Building>>() { }.getType()));

        return map;
    }
}
