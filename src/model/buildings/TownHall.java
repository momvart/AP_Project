package model.buildings;

import model.Builder;
import model.BuilderStatus;

import java.util.ArrayList;

public class TownHall extends VillageBuilding
{
    int goldCapactiy;
    int elixirCapacity;
    ArrayList<Builder> builders;

    public int getGoldCapactiy()
    {
        return goldCapactiy;
    }

    public int getElixirCapacity()
    {
        return elixirCapacity;
    }

    @Override
    public int getType()
    {
        return 5;
    }

    @Override
    public void upgrade()
    {
        super.upgrade();
    }

    public Builder getAvailableBuilder()
    {
        for (Builder builder : builders)
        {
            if (builder.getBuilderStatus() == BuilderStatus.FREE)
                return builder;
        }
        // TODO: 4/13/18 : throw BuilderNotAvailable Exception
        return null;
    }

    public Builder getBuilderByNum(int num)
    {
        for (Builder builder : builders)
        {
            if (builder.getBuilderNum() == num)
                return builder;
        }
        // TODO: 4/13/18 : throw BuilderNotFound Exception
        return null;
    }
}
