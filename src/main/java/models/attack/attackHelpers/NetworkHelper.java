package models.attack.attackHelpers;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import models.Resource;
import models.soldiers.Soldier;
import network.AttackMessage;
import utils.Point;
import utils.PointF;
import utils.TimeSpan;

import java.net.*;

public class NetworkHelper
{
    public static final String UNIT_TYPE_FIELD = "ut";
    public static final String ID_FIELD = "id";
    public static final String SOLDIER_ID_FIELD = "sid";
    public static final String HEALTH_STRENGTH = "hs";
    public static final String GOLD_FIELD = "gold";
    public static final String ELIXIR_FIELD = "elixir";
    //these methods should send some encoded data to the network

    private static DatagramSocket socket;
    private static int port;
    private static InetAddress host;


    private static Gson serializer = new Gson();

    public static void initialize(String host, int port) throws UnknownHostException, SocketException
    {
        socket = new DatagramSocket();
        NetworkHelper.host = InetAddress.getByName(host);
        NetworkHelper.port = port;
    }

    private static boolean checkSocket()
    {
        return socket != null;
    }

    private static void sendAttackMessage(int type, JsonObject data)
    {
        if (socket == null)
            return;
        try
        {
            byte[] message = serializer.toJson(new AttackMessage(type, data)).getBytes();
            DatagramPacket packet = new DatagramPacket(message, 0, message.length, host, port);
            socket.send(packet);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    private static void addPointProperty(JsonObject obj, Point point)
    {
        obj.addProperty("x", point.getX());
        obj.addProperty("y", point.getY());
    }

    private static void addPointProperty(JsonObject obj, PointF point)
    {
        obj.addProperty("x", point.getX());
        obj.addProperty("y", point.getY());
    }

    public static void putUnits(int unitType, int count, Point location)
    {
        if (!checkSocket())
            return;

        JsonObject obj = new JsonObject();
        obj.addProperty(UNIT_TYPE_FIELD, unitType);
        obj.addProperty("count", count);
        addPointProperty(obj, location);
        sendAttackMessage(AttackMessage.Types.PutUnit, obj);
    }

    public static void sldrStJogTowd(long soldierId, PointF dest)
    {
        JsonObject obj = new JsonObject();
        obj.addProperty(ID_FIELD, soldierId);
        obj.addProperty("x", dest.getX());
        obj.addProperty("y", dest.getY());
        sendAttackMessage(AttackMessage.Types.StartJogging, obj);
    }

    public static void grdnGntStJojTowd(long id, Soldier dest)
    {
        JsonObject obj = new JsonObject();
        obj.addProperty(ID_FIELD, id);
        obj.addProperty("sid", dest.getId());
        sendAttackMessage(AttackMessage.Types.GCStartJogging, obj);
    }

    public static void setSldPos(long soldierId, PointF position)
    {
        JsonObject obj = new JsonObject();
        obj.addProperty(ID_FIELD, soldierId);
        addPointProperty(obj, position);
        sendAttackMessage(AttackMessage.Types.SoldierSetPos, obj);
    }

    public static void setGrdnGntPos(long id, PointF position)
    {
        JsonObject obj = new JsonObject();
        obj.addProperty(ID_FIELD, id);
        addPointProperty(obj, position);
        sendAttackMessage(AttackMessage.Types.GCSetPos, obj);
    }

    public static void soldierDie(long id)
    {
        JsonObject obj = new JsonObject();
        obj.addProperty(ID_FIELD, id);
        sendAttackMessage(AttackMessage.Types.SoldierDie, obj);
    }

    public static void buildingDestroy(long id)
    {
        JsonObject obj = new JsonObject();
        obj.addProperty(ID_FIELD, id);
        sendAttackMessage(AttackMessage.Types.BuildingDestroy, obj);
    }

    public static void soldierSetHealth(long id, int health)
    {
        JsonObject obj = new JsonObject();
        obj.addProperty(ID_FIELD, id);
        obj.addProperty(HEALTH_STRENGTH, health);
        sendAttackMessage(AttackMessage.Types.SoldierSetHealth, obj);
    }

    public static void buildingSetStrength(long id, int strength)
    {
        JsonObject obj = new JsonObject();
        obj.addProperty(ID_FIELD, id);
        obj.addProperty(HEALTH_STRENGTH, strength);
        sendAttackMessage(AttackMessage.Types.BuildingSetStrength, obj);
    }

    public static void bulletStartNewWave(long buildingId, PointF start, PointF end, Soldier soldier)
    {
        JsonObject obj = new JsonObject();
        obj.addProperty(ID_FIELD, buildingId);
        obj.addProperty("x1", start.getX());
        obj.addProperty("y1", start.getY());
        obj.addProperty("x2", end.getX());
        obj.addProperty("y2", end.getY());
        if (soldier == null)
            obj.addProperty(SOLDIER_ID_FIELD, -10);
        else
            obj.addProperty(SOLDIER_ID_FIELD, soldier.getId());
        sendAttackMessage(AttackMessage.Types.BulletStartNewWave, obj);
    }

    public static void setClaimedScore(int claimedScore)
    {
        JsonObject obj = new JsonObject();
        obj.addProperty("score", claimedScore);
        sendAttackMessage(AttackMessage.Types.SetScore, obj);
    }

    public static void setClaimedResource(Resource claimedResource)
    {
        JsonObject obj = new JsonObject();
        obj.addProperty(GOLD_FIELD, claimedResource.getGold());
        obj.addProperty(ELIXIR_FIELD, claimedResource.getElixir());
        sendAttackMessage(AttackMessage.Types.SetResource, obj);
    }

    public static void setTime(TimeSpan time)
    {
        JsonObject obj = new JsonObject();
        obj.addProperty("seconds", time.getTotalSeconds());
        sendAttackMessage(AttackMessage.Types.SetTime, obj);
    }
}
