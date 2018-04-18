package models.buildings;

import menus.AvailableBuildingsSubmenu;
import menus.Menu;
import menus.ParentMenu;
import menus.Submenu;
import models.Builder;
import models.BuilderStatus;
import models.World;
import utils.Point;

import java.util.ArrayList;

public class TownHall extends VillageBuilding
{
    ArrayList<Builder> builders;

    public TownHall(Point location)
    {
        super(location);
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

    @Override
    public Submenu getMenu(ParentMenu parent)
    {
        Submenu menu = super.getMenu(parent);
        menu.insertItem(new AvailableBuildingsSubmenu(menu, World.sCurrentGame.getVillage()))
                .insertItem(new Menu(Menu.Id.TH_STATUS, "Status"));
        return menu;
    }
}
