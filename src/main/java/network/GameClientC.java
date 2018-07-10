package network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import models.World;
import models.attack.AttackMap;
import models.buildings.Building;
import models.buildings.ElixirStorage;
import models.buildings.GoldStorage;
import serialization.AttackMapGlobalAdapter;
import serialization.BuildingGlobalAdapter;
import serialization.StorageGlobalAdapter;

import java.io.IOException;
import java.net.Socket;
import java.util.*;

//GameClient that is used in client side
public class GameClientC extends GameClient
{
    private HashMap<UUID, ClientInfo> players;

    private IOnChatMessageReceivedListener chatMessageReceiver;
    private IOnAttackMapReturnedListener attackMapReturnedListener;
    private Runnable onPlayersListUpdatedListener;

    public GameClientC(int port, String ip) throws IOException
    {
        super(new Socket(ip, port));
    }

    private void callOnChatMessageReceived(Message chatMessage)
    {
        if (chatMessageReceiver != null)
            chatMessageReceiver.onChatMessageReceived(players.get(chatMessage.getSenderId()).getName(), chatMessage.getMessage());
    }

    public void setChatMessageReceiver(IOnChatMessageReceivedListener chatMessageReceiver)
    {
        this.chatMessageReceiver = chatMessageReceiver;
    }

    private void callOnAttackMapReturned(UUID from, AttackMap map)
    {
        if (attackMapReturnedListener != null)
            attackMapReturnedListener.onAttackMapReturned(from, map);
    }

    public void setAttackMapReturnedListener(IOnAttackMapReturnedListener attackMapReturnedListener)
    {
        this.attackMapReturnedListener = attackMapReturnedListener;
    }

    private void callOnPlayersListUpdated()
    {
        if (onPlayersListUpdatedListener != null)
            onPlayersListUpdatedListener.run();
    }

    public void setOnPlayersListUpdatedListener(Runnable onPlayersListUpdatedListener)
    {
        this.onPlayersListUpdatedListener = onPlayersListUpdatedListener;
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
                this.setInfo(deserializer.fromJson(message.getMessage(), ClientInfo.class));
                break;
            case PLAYERS_LIST:
                players = new HashMap<>();
                ((ArrayList<ClientInfo>)deserializer.fromJson(message.getMessage(), new TypeToken<ArrayList<ClientInfo>>() { }.getType())).forEach(info -> players.put(info.getId(), info));
                callOnPlayersListUpdated();
                break;
            case CHAT_MESSAGE:
                callOnChatMessageReceived(message);
                break;
            case ATTACK_REQUEST:
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
        sendMessage(new Message(deserializer.toJson(getInfo()), getClientId(), MessageType.SET_CLIENT_INFO));
    }
}
