package serialization;

import com.google.gson.*;
import models.buildings.BuildingInfo;
import models.buildings.DefensiveTowerInfo;
import models.buildings.VillageBuildingInfo;

import java.lang.reflect.Type;

public class BuildingInfoAdapter implements JsonSerializer<BuildingInfo>, JsonDeserializer<BuildingInfo>
{
    private final static String INFO_TYPE_FIELD = "infoType";

    @Override
    public BuildingInfo deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException
    {
        Class cls = json.getAsJsonObject().get(INFO_TYPE_FIELD).getAsInt() == 0 ? VillageBuildingInfo.class : DefensiveTowerInfo.class;
        return context.deserialize(json, cls);
    }

    @Override
    public JsonElement serialize(BuildingInfo src, Type typeOfSrc, JsonSerializationContext context)
    {
        JsonObject retVal = context.serialize(src).getAsJsonObject();
        retVal.add(INFO_TYPE_FIELD, new JsonPrimitive(src instanceof VillageBuildingInfo ? 0 : 1));
        return retVal;
    }
}
