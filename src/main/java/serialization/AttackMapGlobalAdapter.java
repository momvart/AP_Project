package serialization;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import models.Map;
import models.attack.AttackMap;
import models.Resource;
import models.buildings.Building;
import models.buildings.Wall;
import utils.Size;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class AttackMapGlobalAdapter implements JsonDeserializer<AttackMap>, JsonSerializer<Map>
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

    @Override
    public JsonElement serialize(Map src, Type typeOfSrc, JsonSerializationContext context)
    {
        JsonObject retVal = new JsonObject();

        JsonArray jsonSize = new JsonArray(2);
        jsonSize.add(src.getWidth()); jsonSize.add(src.getHeight());
        retVal.add(SIZE_FIELD_NAME, jsonSize);

        //As resources are recalculated we put zero
        retVal.add(RESOURCES_FIELD_NAME, context.serialize(new Resource(0, 0)));


        JsonArray walls = new JsonArray(src.getBuildings(Wall.BUILDING_TYPE).size());
        src.getBuildings(Wall.BUILDING_TYPE).getValues().stream().map(wall ->
        {
            JsonObject obj = context.serialize(wall, Building.class).getAsJsonObject();
            obj.remove("type");
            return obj;
        }).forEach(walls::add);
        retVal.add(WALLS_FIELD_NAME, walls);

        JsonArray buildings = new JsonArray();
        src.getAllBuildings().filter(building -> building.getType() != Wall.BUILDING_TYPE)
                .map(building -> context.serialize(building, Building.class))
                .forEach(buildings::add);
        retVal.add(BUILDINGS_FIELD_NAME, buildings);

        return retVal;
    }
}
