package network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import javafx.util.Pair;
import models.World;
import models.attack.AttackMap;
import models.attack.AttackReport;
import models.buildings.Building;
import models.buildings.ElixirStorage;
import models.buildings.GoldStorage;
import serialization.AttackMapGlobalAdapter;
import serialization.BuildingGlobalAdapter;
import serialization.StorageGlobalAdapter;

import java.io.IOException;
import java.net.Socket;
import java.util.*;
import java.util.function.Consumer;

//GameClient that is used in client side
public class GameClientC extends GameClient
{
    private HashMap<UUID, ClientInfo> players;

    private IOnChatMessageReceivedListener chatMessageReceiver;
    private IOnAttackMapReturnedListener attackMapReturnedListener;
    private Runnable playersListUpdatedListener;
    private Consumer<UUID> attackStartListener;
    private Consumer<UUID> defenseStartListener;
    private Consumer<AttackReport> attackFinishListener;
    private Consumer<AttackReport> defenseFinishListener;

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

    private void callDefenseStarted(UUID attackerId)
    {
        if (defenseStartListener != null)
            defenseStartListener.accept(attackerId);
    }

    public void setDefenseStartListener(Consumer<UUID> defenseStartListener)
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
                        .registerTypeAdapter(Map.class, new AttackMapGlobalAdapter())
                        .registerTypeAdapter(Building.class, new BuildingGlobalAdapter())
                        .setPrettyPrinting()
                        .create();
                sendMessage(new Message(serializer.toJson(World.getVillage().getMap(), Map.class), getClientId(), MessageType.RET_MAP).setMetadata(message.getSenderId().toString()));
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
                    callDefenseStarted(pair.getKey());
            }
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
}
