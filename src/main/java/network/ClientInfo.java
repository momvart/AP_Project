package network;

import java.util.UUID;

public class ClientInfo
{
    private UUID id;
    private String name;

    public ClientInfo()
    {
        id = UUID.randomUUID();
    }

    public UUID getId()
    {
        return id;
    }

    public void setId(UUID id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }
}
