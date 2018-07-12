package models.attack.attackHelpers;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import models.soldiers.Soldier;
import network.AttackMessage;
import utils.Point;
import utils.PointF;

import java.net.*;

public class NetworkHelper
{
    private static final String UNIT_TYPE_FIELD = "UT";
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

    public static void putUnits(int unitType, int count, Point location)
    {
        if (!checkSocket())
            return;

        JsonObject obj = new JsonObject();
        obj.addProperty(UNIT_TYPE_FIELD, unitType);
        obj.addProperty("count", count);
        obj.addProperty("x", location.getX());
        obj.addProperty("y", location.getY());
        sendAttackMessage(AttackMessage.Types.PutUnit, obj);
    }

    public static void soldrStJogTowd(long soldierId, PointF dest)
    {

    }

    public static void grdnGntStJojTow(long id, Soldier dest)
    {

    }

    public static void setSldPos(long soldierId, PointF position)
    {

    }

    public static void soldierDie(long id)
    {

    }

    public static void buildingDestroy(long id)
    {

    }

    public static void soldierDecreaseHealth(long id, int amount)
    {

    }

    public static void soldierIncreaseHealth(long id, int amount)
    {

    }

    public static void buildingDecreaseStrength(long id, int amount)
    {

    }

    public static void bulletStartNewWave(long buildingId, PointF start, PointF end, Soldier soldier)
    {

    }

    public static void setgrdnGntPos(long id, PointF position)
    {

    }
}
