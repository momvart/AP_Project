package models.buildings;

import models.Builder;
import models.BuilderStatus;

import java.util.ArrayList;

public class TownHall extends VillageBuilding
{
    ArrayList<Builder> builders;


    @Override
    public int getType()
    {
        return 5;
    }

    @Override
    public void upgrade()
    {
        super.upgrade();
        addBuilder();
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

    private void addBuilder()
    {
        if (builders.size() < this.level / 5 + 1)
        {
            Builder builder = new Builder(builders.size() + 1);
            builders.add(builder);
        }
    }
}
