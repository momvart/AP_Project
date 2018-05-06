package serialization;

import com.google.gson.*;
import models.buildings.Storage;

import java.lang.reflect.*;

public class StorageGlobalAdapter<T extends Storage> implements JsonDeserializer<T>
{
    private TypeAdapter<T> defAdapter;

    private Field amountField;

    public StorageGlobalAdapter(Class<T> clazz)
    {
        this.defAdapter = new Gson().getAdapter(clazz);
        try
        {
            amountField = Storage.class.getDeclaredField("currentAmount");
            amountField.setAccessible(true);
        }
        catch (NoSuchFieldException e)
        {
            e.printStackTrace();
        }
    }


    @Override
    public T deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException
    {
        T storage = defAdapter.fromJsonTree(json);
        try
        {
            amountField.set(storage, json.getAsJsonObject().get("amount").getAsInt());
        }
        catch (IllegalAccessException e)
        {
            e.printStackTrace();
        }
        return storage;
    }
}
