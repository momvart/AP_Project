package network;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import javafx.util.Pair;
import models.World;
import models.attack.AttackMap;
import models.attack.AttackReport;
import models.attack.attackHelpers.NetworkHelper;
import models.buildings.Building;
import models.buildings.ElixirStorage;
import models.buildings.GoldStorage;
import models.soldiers.Soldier;
import serialization.AttackMapGlobalAdapter;
import serialization.BuildingGlobalAdapter;
import serialization.StorageGlobalAdapter;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

//GameClient that is used in client side
public class GameClientC extends GameClient
{
    private HashMap<UUID, ClientInfo> players;

    private IOnChatMessageReceivedListener chatMessageReceiver;
    private IOnAttackMapReturnedListener attackMapReturnedListener;
    private Runnable playersListUpdatedListener;
    private Consumer<UUID> attackStartListener;
    private BiConsumer<UUID, List<Soldier>> defenseStartListener;
    private Consumer<AttackReport> attackFinishListener;
    private Consumer<AttackReport> defenseFinishListener;
    private Consumer<ArrayList<AttackReport>> attackReportsReceivedListener;

    public GameClientC(int port, String ip) throws IOException
    {
        super(new Socket(ip, port));
    }

    private void callOnChatMessageReceived(Message chatMessage)
    {
        if (chatMessageReceiver != null)
            chatMessageReceiver.onChatMessageReceived(chatMessage.getSenderId(), chatMessage.getMessage());
    }

    public void setChatMessageReceiver(IOnChatMessageReceivedListener chatMessageReceiver)
    {
        this.chatMessageReceiver = chatMessageReceiver;
    }

    private UUID activeAttackTarget;

    public UUID getActiveAttackTarget()
    {
        return activeAttackTarget;
    }

    private void callOnAttackMapReturned(UUID from, AttackMap map)
    {
        activeAttackTarget = from;
        if (attackMapReturnedListener != null)
            attackMapReturnedListener.onAttackMapReturned(from, map);
    }

    public void setAttackMapReturnedListener(IOnAttackMapReturnedListener attackMapReturnedListener)
    {
        this.attackMapReturnedListener = attackMapReturnedListener;
    }

    private void callOnPlayersListUpdated()
    {
        if (playersListUpdatedListener != null)
            playersListUpdatedListener.run();
    }

    public void setPlayersListUpdatedListener(Runnable playersListUpdatedListener)
    {
        this.playersListUpdatedListener = playersListUpdatedListener;
    }

    private void callAttackStarted(UUID defenderId)
    {
        if (attackStartListener != null)
            attackStartListener.accept(defenderId);
    }

    public void setAttackStartListener(Consumer<UUID> attackStartListener)
    {
        this.attackStartListener = attackStartListener;
    }

    private void callDefenseStarted(UUID attackerId, List<Soldier> soldiers)
    {
        if (defenseStartListener != null)
            defenseStartListener.accept(attackerId, soldiers);
    }

    public void setDefenseStartListener(BiConsumer<UUID, List<Soldier>> defenseStartListener)
    {
        this.defenseStartListener = defenseStartListener;
    }

    private void callAttackFinished(AttackReport report)
    {
        if (attackFinishListener != null)
            attackFinishListener.accept(report);
    }

    public void setAttackFinishListener(Consumer<AttackReport> attackFinishListener)
    {
        this.attackFinishListener = attackFinishListener;
    }

    private void callDefenseFinished(AttackReport report)
    {
        if (defenseFinishListener != null)
            defenseFinishListener.accept(report);
    }

    public void setDefenseFinishListener(Consumer<AttackReport> defenseFinishListener)
    {
        this.defenseFinishListener = defenseFinishListener;
    }

    private void callAttackReportsReceived(ArrayList<AttackReport> reports)
    {
        if (attackReportsReceivedListener != null)
            attackReportsReceivedListener.accept(reports);
    }

    public void setAttackReportsReceivedListener(Consumer<ArrayList<AttackReport>> attackReportsReceivedListener)
    {
        this.attackReportsReceivedListener = attackReportsReceivedListener;
    }

    public ClientInfo getPlayerInfo(UUID id)
    {
        return players.get(id);
    }

    public Collection<ClientInfo> getPlayersList()
    {
        return players.values();
    }

    @Override
    public void messageReceived(Message message)
    {
        switch (message.getMessageType())
        {
            case SET_ID:
                this.info.setId(UUID.fromString(message.getMessage()));
                break;
            case SET_CLIENT_INFO:
                this.setInfo(gson.fromJson(message.getMessage(), ClientInfo.class));
                break;
            case PLAYERS_LIST:
                players = new HashMap<>();
                ((ArrayList<ClientInfo>)gson.fromJson(message.getMessage(), new TypeToken<ArrayList<ClientInfo>>() { }.getType())).forEach(info -> players.put(info.getId(), info));
                callOnPlayersListUpdated();
                break;
            case CHAT_MESSAGE:
                callOnChatMessageReceived(message);
                break;
            case GET_MAP:
            {
                Gson serializer = new GsonBuilder()
                        .registerTypeAdapter(models.Map.class, new AttackMapGlobalAdapter())
                        .registerTypeAdapter(Building.class, new BuildingGlobalAdapter())
                        .setPrettyPrinting()
                        .create();
                sendMessage(new Message(serializer.toJson(World.getVillage().getMap(), models.Map.class), getClientId(), MessageType.RET_MAP).setMetadata(message.getSenderId().toString()));
            }
            break;
            case RET_MAP:
            {
                Gson deserializer = new GsonBuilder()
                        .registerTypeAdapter(AttackMap.class, new AttackMapGlobalAdapter())
                        .registerTypeAdapter(Building.class, new BuildingGlobalAdapter())
                        .registerTypeAdapter(GoldStorage.class, new StorageGlobalAdapter<>(GoldStorage.class))
                        .registerTypeAdapter(ElixirStorage.class, new StorageGlobalAdapter<>(ElixirStorage.class))
                        .create();
                AttackMap map = deserializer.fromJson(message.getMessage(), AttackMap.class);
                callOnAttackMapReturned(message.getSenderId(), map);
            }
            break;
            case ATTACK_REPORT:
            {
                AttackReport report = gson.fromJson(message.getMessage(), AttackReport.class);

                if (report.getAttackerId().equals(getClientId()))
                    callAttackFinished(report);
                else
                    callDefenseFinished(report);
            }
            break;
            case ATTACK_STARTED:
            {
                Pair<UUID, UUID> pair = gson.fromJson(message.getMessage(), new TypeToken<Pair<UUID, UUID>>() { }.getType());
                if (pair.getKey().equals(getClientId()))
                    callAttackStarted(pair.getValue());
                else
                    callDefenseStarted(pair.getKey(), gson.fromJson(message.getMetadata(), new TypeToken<ArrayList<Soldier>>() { }.getType()));
            }
            break;
            case ATTACK_UDP_READY:
                try
                {
                    //TODO: set up network helper
                    JsonObject obj = new JsonParser().parse(message.getMessage()).getAsJsonObject();
                    NetworkHelper.initialize(obj.get("host").getAsString(), obj.get("port").getAsInt());
                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                }
                break;
            case ATTACK_REPORTS_LIST:
                callAttackReportsReceived(gson.fromJson(message.getMessage(), new TypeToken<ArrayList<AttackReport>>() { }.getType()));
                break;
            default:
                super.messageReceived(message);
        }
    }

    public void sendChatMessage(String message)
    {
        sendMessage(new Message(message, getClientId(), MessageType.CHAT_MESSAGE));
    }

    public void sendInfo()
    {
        sendMessage(new Message(gson.toJson(getInfo()), getClientId(), MessageType.SET_CLIENT_INFO));
    }

    public void sendAttackReport(AttackReport report)
    {
        sendMessage(gson.toJson(report), MessageType.ATTACK_REPORT);
    }

    public void sendUDPStarted(String host, int port)
    {
        JsonObject obj = new JsonObject();
        obj.addProperty("host", host);
        obj.addProperty("port", port);
        sendMessage(gson.toJson(obj), MessageType.ATTACK_UDP_READY);
    }
}
