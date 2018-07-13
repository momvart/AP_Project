package network;

import com.google.gson.JsonObject;
import models.attack.attackHelpers.NetworkHelper;
import utils.PointF;

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

    public long getLongData(String name)
    {
        return getData().get(name).getAsLong();
    }

    public long getIdData() {return getData().get(NetworkHelper.ID_FIELD).getAsLong();}

    public double getDoubleData(String name)
    {
        return getData().get(name).getAsDouble();
    }

    public PointF getPointFData() {return new PointF(getDoubleData("x"), getDoubleData("y"));}

    public static class Types
    {
        public static final int PutUnit = 1001;
        public static final int StartJogging = 1002;
        public static final int GCStartJogging = 10021;
        public static final int SoldierSetPos = 1003;
        public static final int GCSetPos = 10031;
        public static final int SoldierDie = 1004;
        public static final int BuildingDestroy = 1005;
        public static final int SoldierSetHealth = 1006;
        public static final int BuildingSetStrength = 1007;
        public static final int BulletStartNewWave = 1008;
        public static final int BuildingSetTarget = 1009;
        public static final int SoldierSetTarget = 1010;
    }
}
