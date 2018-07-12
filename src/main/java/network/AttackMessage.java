package network;

import com.google.gson.JsonObject;

public class AttackMessage
{
    private int type;
    private JsonObject data;

    public AttackMessage(int type, JsonObject data)
    {
        this.type = type;
        this.data = data;
    }

    public int getType()
    {
        return type;
    }

    public JsonObject getData()
    {
        return data;
    }

    public int getIntData(String name)
    {
        return getData().get(name).getAsInt();
    }

    public static class Types
    {
        public static final int PutUnit = 1001;
        public static final int StartJogging = 1002;
        public static final int SoldierSetPos = 1003;
        public static final int SoldierDie = 1004;
        public static final int BuildingDestroy = 1005;
        public static final int SoldierSetHealth = 1006;
        public static final int BuildingSetStrength = 1007;
        public static final int BuildingStartNewWave = 1008;
    }
}
