package network;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.function.Consumer;

public class AttackUDPReceiver extends UDPReceiver
{
    public AttackUDPReceiver(int port)
    {
        super(port);
    }

    private JsonParser deserializer = new JsonParser();
    private Consumer<AttackMessage> attackMessageReceiver;

    @Override
    protected void callOnMessageReceived(String message)
    {
        super.callOnMessageReceived(message);
        try
        {
            System.err.println("received: " + message);
            if (attackMessageReceiver == null)
                return;
            JsonObject object = deserializer.parse(message).getAsJsonObject();
            attackMessageReceiver.accept(new AttackMessage(object.get("type").getAsInt(), object.get("data").getAsJsonObject()));
        }
        catch (Exception ignored) {}
    }

    public void setAttackMessageReceiver(Consumer<AttackMessage> attackMessageReceiver)
    {
        this.attackMessageReceiver = attackMessageReceiver;
    }
}
